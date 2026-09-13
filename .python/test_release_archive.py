import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from release_archive import ReleaseError, archive_release, collect


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


if __name__ == "__main__":
    unittest.main()
