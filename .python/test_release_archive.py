import json
import os
from pathlib import Path
import re
import shutil
import tempfile
import unittest
from unittest.mock import patch
import uuid

from release_archive import ReleaseError, archive_release, collect


def dacl_entries(path):
    """(control flags, ACE strings) of the Windows DACL of path, e.g. ("AI", ["A;ID;FA;;;SY", ...])."""
    import ctypes
    from ctypes import wintypes
    advapi32 = ctypes.WinDLL("advapi32", use_last_error=True)
    kernel32 = ctypes.WinDLL("kernel32", use_last_error=True)
    get = advapi32.GetNamedSecurityInfoW
    get.argtypes = (wintypes.LPCWSTR, ctypes.c_int, wintypes.DWORD) + (ctypes.c_void_p,) * 4 + (ctypes.POINTER(ctypes.c_void_p),)
    get.restype = wintypes.DWORD
    convert = advapi32.ConvertSecurityDescriptorToStringSecurityDescriptorW
    convert.argtypes = (ctypes.c_void_p, wintypes.DWORD, wintypes.DWORD, ctypes.POINTER(wintypes.LPWSTR), ctypes.c_void_p)
    convert.restype = wintypes.BOOL
    kernel32.LocalFree.argtypes = (ctypes.c_void_p,)
    descriptor, text = ctypes.c_void_p(), wintypes.LPWSTR()
    error = get(str(path), 1, 4, None, None, None, None, ctypes.byref(descriptor))  # SE_FILE_OBJECT, DACL only
    if error:
        raise OSError(None, ctypes.FormatError(error), str(path), error)
    try:
        if not convert(descriptor, 1, 4, ctypes.byref(text), None):  # SDDL_REVISION_1, DACL only
            raise ctypes.WinError(ctypes.get_last_error())
        flags, entries = re.fullmatch(r"D:([A-Z]*)((?:\([^()]*\))*)", text.value).groups()
    finally:
        kernel32.LocalFree(ctypes.cast(text, ctypes.c_void_p))
        kernel32.LocalFree(descriptor)
    return flags, re.findall(r"\(([^()]*)\)", entries)


def mkdtemp_is_restricted():
    """Whether tempfile.mkdtemp() gives a new directory a protected DACL (Windows since Python 3.12.4 and 3.13)."""
    if os.name != "nt":
        return False
    path = tempfile.mkdtemp()
    try:
        return "P" in dacl_entries(path)[0]
    finally:
        os.rmdir(path)


class ReleaseMatrixTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.output = self.root / "app/build/outputs/apk/release"
        self.output.mkdir(parents=True)
        self.apk = self.output / "plugin.apk"
        self.apk.write_bytes(b"signed-apk-fixture")
        self.element = {"filters": [], "versionCode": 7, "versionName": "1.0.0", "outputFile": self.apk.name}
        self.metadata = {"variantName": "release", "applicationId": "test.plugin", "elements": [self.element]}
        self.expected = {"name": "release", "applicationId": "test.plugin", "nativeAbis": [], "outputs": [{"abi": "universal", "versionCode": 7, "versionName": "1.0.0"}]}
        self.manifest = {"apkRoot": str(self.output.parent), "variants": [self.expected], "projectRoot": str(self.root), "projectName": "autojs6-plugin-example", "destination": str(self.root / "releases/v1.0.0"), "signerSha256": "test"}
        self.save()

    def save(self):
        (self.output / "output-metadata.json").write_text(json.dumps(self.metadata), encoding="utf-8")

    def test_version_name_flavor_is_not_duplicated(self):
        self.expected["name"] = self.metadata["variantName"] = "mobileEnRelease"
        self.element["versionName"] = self.expected["outputs"][0]["versionName"] = "1.0.0-mobile-en"
        self.save()
        with patch("release_archive.verify_apk", return_value=[]):
            archive_release(self.manifest)
        receipt = json.loads((Path(self.manifest["destination"]) / "release-manifest.json").read_text())
        name = receipt["artifacts"][0]["file"]
        self.assertTrue(name.startswith("autojs6-plugin-example-v1.0.0-mobile-en-universal-"), name)

    def test_variants_sharing_version_name_remain_distinguishable(self):
        self.expected["name"] = self.metadata["variantName"] = "mobileRelease"
        self.save()
        other = self.output.parent / "serverRelease"
        other.mkdir()
        (other / "plugin.apk").write_bytes(self.apk.read_bytes())
        data = {**self.metadata, "variantName": "serverRelease"}
        (other / "output-metadata.json").write_text(json.dumps(data))
        self.manifest["variants"].append({**self.expected, "name": "serverRelease"})
        with patch("release_archive.verify_apk", return_value=[]):
            archive_release(self.manifest)
        receipt = json.loads((Path(self.manifest["destination"]) / "release-manifest.json").read_text())
        names = {item["file"] for item in receipt["artifacts"]}
        self.assertEqual(2, len(names))
        for flavor in ["mobile", "server"]:
            self.assertTrue(any(name.startswith("autojs6-plugin-example-v1.0.0-" + flavor + "-universal-") for name in names))

    def test_exact_matrix(self):
        self.assertEqual(1, len(collect(self.manifest)))

    def test_missing_variant(self):
        self.manifest["variants"].append({**self.expected, "name": "serverRelease"})
        with self.assertRaisesRegex(ReleaseError, "variants differ"):
            collect(self.manifest)

    def test_missing_abi(self):
        self.expected["outputs"].append({**self.expected["outputs"][0], "abi": "arm64-v8a"})
        with self.assertRaisesRegex(ReleaseError, "Incomplete ABI"):
            collect(self.manifest)

    def test_stale_version(self):
        self.element["versionCode"] = 6
        self.save()
        with self.assertRaisesRegex(ReleaseError, "Stale version"):
            collect(self.manifest)

    def test_duplicate_abi(self):
        self.metadata["elements"].append(self.element)
        self.save()
        with self.assertRaisesRegex(ReleaseError, "Duplicate ABI"):
            collect(self.manifest)

    def test_unlisted_apk(self):
        (self.output / "old.apk").write_bytes(b"old")
        with self.assertRaisesRegex(ReleaseError, "Unlisted APK"):
            collect(self.manifest)

    def test_path_escape(self):
        self.element["outputFile"] = "../outside.apk"
        self.save()
        with self.assertRaisesRegex(ReleaseError, "leaves allowed"):
            collect(self.manifest)

    def test_failed_verification_preserves_archive(self):
        destination = Path(self.manifest["destination"])
        destination.mkdir(parents=True)
        retained = destination / "user-file.txt"
        retained.write_text("preserve", encoding="utf-8")
        with patch("release_archive.verify_apk", side_effect=ReleaseError("signature mismatch")):
            with self.assertRaisesRegex(ReleaseError, "signature mismatch"):
                archive_release(self.manifest)
        self.assertEqual("preserve", retained.read_text(encoding="utf-8"))

    def test_archive_checksums_and_safe_repeat(self):
        with patch("release_archive.verify_apk", return_value=[]):
            archive_release(self.manifest)
            archive_release(self.manifest)
        destination = Path(self.manifest["destination"])
        receipt = json.loads((destination / "release-manifest.json").read_text(encoding="utf-8"))
        self.assertEqual(1, len(receipt["artifacts"]))
        self.assertEqual(self.apk.read_bytes(), (destination / receipt["artifacts"][0]["file"]).read_bytes())

    def test_unmanaged_directory_is_preserved(self):
        destination = Path(self.manifest["destination"])
        destination.mkdir(parents=True)
        (destination / "old.apk").write_bytes(b"old")
        with patch("release_archive.verify_apk", return_value=[]):
            with self.assertRaisesRegex(ReleaseError, "unmanaged"):
                archive_release(self.manifest)

    @unittest.skipUnless(mkdtemp_is_restricted(), "tempfile.mkdtemp() does not restrict new directories here")
    def test_archive_has_the_access_rights_of_a_plain_directory(self):
        # The archive is staged with mkdtemp(), which used to leave only SYSTEM, Administrators and OWNER RIGHTS on it.
        project = Path(tempfile.gettempdir()) / ("autojs6-release-acl-" + uuid.uuid4().hex)
        reference = project / "releases" / "plain"
        reference.mkdir(parents=True)
        self.addCleanup(shutil.rmtree, project)
        (reference / "plain.apk").write_bytes(b"")
        destination = project / "releases" / "v1.0.0"
        self.manifest.update(projectRoot=str(project), destination=str(destination))
        with patch("release_archive.verify_apk", return_value=[]):
            archive_release(self.manifest)
        self.assertNotIn("P", dacl_entries(destination)[0], "the DACL is still protected from inheritance")
        self.assertEqual(dacl_entries(reference)[1], dacl_entries(destination)[1])
        for item in destination.iterdir():
            self.assertEqual(dacl_entries(reference / "plain.apk")[1], dacl_entries(item)[1], item.name)


if __name__ == "__main__":
    unittest.main()
