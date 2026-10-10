"""Record, check or refresh the OpenCV 4.8.0 Java AAR used by this plugin.

The plugin calls the org.opencv Java API inside its own process. Its AAR keeps the vendor Java
classes and resources unchanged, while every `jni/<abi>/libopencv_java4.so` is rebuilt from the
upstream OpenCV 4.8.0 source with NDK r28c and 16 KB ELF segment alignment by
`tools/opencv/build_opencv_java.ps1`. No other repository supplies these binaries.

Commands:
  check                  Compare the AAR with libs/org-opencv-4_8_0/opencv-4.8.0.provenance.json.
  record                 Rewrite the provenance record from the current AAR (needs the NDK).
  replace --native-dir D Replace the JNI entries with D/<abi>/libopencv_java4.so, then record.
"""
import argparse
import datetime
import hashlib
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import zipfile

ROOT = Path(__file__).resolve().parents[2]
AAR = ROOT / "libs/org-opencv-4_8_0/opencv-4.8.0.aar"
RECORD = AAR.with_suffix(".provenance.json")
ABIS = ("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
LIBRARY = "libopencv_java4.so"
NDK_VERSION = "28.2.13676358"
RELEASE_ZONE = datetime.timezone(datetime.timedelta(hours=8))
UPSTREAM = {
    "repository": "https://github.com/opencv/opencv",
    "tag": "4.8.0",
    "tagObject": "53296de62872b5e7d042ddffb49679fbdcca99f6",
    "commit": "f9a59f2592993d3dcc080e495f4f5e02dd8ec7ef",
}
# Kept in sync with tools/opencv/build_opencv_java.ps1.
BUILD = {
    "script": "tools/opencv/build_opencv_java.ps1",
    "target": "opencv_java",
    "androidNdkVersion": NDK_VERSION,
    "androidNdkRelease": "r28c",
    "cmakeVersion": "3.22.1",
    "androidMinSdk": 24,
    "stl": "c++_shared",
    "options": [
        "-DCMAKE_BUILD_TYPE=Release", "-DCMAKE_SHARED_LINKER_FLAGS=-Wl,-z,max-page-size=16384",
        "-DBUILD_JAVA=ON", "-DBUILD_ANDROID_PROJECTS=ON", "-DBUILD_KOTLIN_EXTENSIONS=OFF", "-DWITH_TBB=ON",
        "-DWITH_OPENCL=OFF", "-DWITH_IPP=ON (x86, x86_64) / OFF (ARM)", "-DBUILD_TESTS=OFF", "-DBUILD_PERF_TESTS=OFF",
        "-DBUILD_EXAMPLES=OFF", "-DBUILD_DOCS=OFF", "-DANDROID_STL=c++_shared", "-DANDROID_NATIVE_API_LEVEL=24",
    ],
    "strip": "llvm-strip",
}


def sha256(data):
    return hashlib.sha256(data).hexdigest()


def readelf():
    sdk = os.environ.get("ANDROID_HOME") or os.environ.get("ANDROID_SDK_ROOT")
    if not sdk:
        properties = ROOT / "local.properties"
        line = next((l for l in properties.read_text(encoding="utf-8").splitlines() if l.startswith("sdk.dir=")), None) \
            if properties.is_file() else None
        if line:
            sdk = line.split("=", 1)[1].replace("\\:", ":").replace("\\\\", "\\")
    if not sdk:
        raise SystemExit("Set ANDROID_HOME or sdk.dir in local.properties")
    tools = list((Path(sdk) / "ndk" / NDK_VERSION / "toolchains/llvm/prebuilt").glob("*/bin/llvm-readelf*"))
    if not tools:
        raise SystemExit(f"NDK {NDK_VERSION} is not installed under {sdk}")
    return str(tools[0])


def elf_facts(tool, data, scratch):
    scratch.write_bytes(data)
    def run(*options):
        return subprocess.check_output([tool, *options, str(scratch)], text=True, errors="replace")
    notes = run("-n")
    build_id = re.search(r"Build ID: ([0-9a-f]+)", notes)
    ident = re.search(r"NT_ANDROID_TYPE_IDENT\s+description data: ([0-9a-f ]+)", notes)
    ndk = None
    if ident:
        raw = bytes(int(value, 16) for value in ident.group(1).split())
        ndk = {"apiLevel": int.from_bytes(raw[0:4], "little"),
               "ndkVersion": raw[4:68].split(b"\0")[0].decode(), "ndkBuildNumber": raw[68:132].split(b"\0")[0].decode()}
    loads = [int(value, 16) for value in re.findall(r"^\s*LOAD\s.*\s(0x[0-9a-f]+)\s*$", run("-l", "-W"), re.M)]
    needed = re.findall(r"\(NEEDED\)\s+Shared library: \[([^\]]+)\]", run("-d"))
    return {"sizeBytes": len(data), "sha256": sha256(data), "gnuBuildId": build_id.group(1) if build_id else None,
            "androidIdent": ndk, "loadSegmentAlignments": loads, "neededLibraries": needed}


def build_record(date):
    data = AAR.read_bytes()
    tool = readelf()
    scratch = ROOT / "build/opencv-java-aar.tmp.so"
    scratch.parent.mkdir(parents=True, exist_ok=True)
    libraries = {}
    with zipfile.ZipFile(AAR) as archive:
        names = set(archive.namelist())
        other = sha256(b"".join(name.encode() + archive.read(name) for name in sorted(names)
                                if not (name.startswith("jni/") and name.endswith(".so"))))
        for abi in ABIS:
            libraries[abi] = elf_facts(tool, archive.read(f"jni/{abi}/{LIBRARY}"), scratch)
    scratch.unlink()
    for abi, facts in libraries.items():
        if abi in ("arm64-v8a", "x86_64") and min(facts["loadSegmentAlignments"]) < 16384:
            raise SystemExit(f"{abi} {LIBRARY} is not 16 KB aligned")
    return {
        "schemaVersion": 3,
        "component": "OpenCV",
        "opencvVersion": "4.8.0",
        "librariesBuiltOn": date,
        "upstream": UPSTREAM,
        "aar": {"path": AAR.relative_to(ROOT).as_posix(), "sha256": sha256(data),
                "javaClassesAndResourcesSha256": other,
                "javaClassesAndResources": "vendor OpenCV 4.8.0 Android Java module, unchanged; only jni/ entries are rebuilt"},
        "build": BUILD,
        "libraries": libraries,
    }


def compare(record):
    data = AAR.read_bytes()
    problems = []
    if record["aar"]["sha256"] != sha256(data):
        problems.append("AAR SHA-256 differs from the record")
    with zipfile.ZipFile(AAR) as archive:
        for abi in ABIS:
            if sha256(archive.read(f"jni/{abi}/{LIBRARY}")) != record["libraries"][abi]["sha256"]:
                problems.append(f"{abi} {LIBRARY} differs from the record")
    return problems


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("command", choices=["check", "record", "replace"])
    parser.add_argument("--native-dir", type=Path, help="Directory with <abi>/libopencv_java4.so (replace)")
    parser.add_argument("--date", help="Day the JNI libraries were built, YYYY-MM-DD (default: today in UTC+08:00)")
    args = parser.parse_args()
    date = args.date or datetime.datetime.now(RELEASE_ZONE).date().isoformat()
    if args.command == "check":
        problems = compare(json.loads(RECORD.read_text(encoding="utf-8")))
        for problem in problems:
            print(problem, file=sys.stderr)
        print("OpenCV AAR matches its provenance record" if not problems else "OpenCV AAR check failed")
        return 1 if problems else 0
    if args.command == "replace":
        if not args.native_dir:
            parser.error("replace needs --native-dir")
        replacements = {f"jni/{abi}/{LIBRARY}": (args.native_dir / abi / LIBRARY).read_bytes() for abi in ABIS}
        with zipfile.ZipFile(AAR) as archive:
            entries = [(info, archive.read(info.filename)) for info in archive.infolist()]
        output = AAR.with_suffix(".tmp")
        with zipfile.ZipFile(output, "w") as archive:
            for info, data in entries:
                archive.writestr(info, replacements.get(info.filename, data))
        output.replace(AAR)
    RECORD.write_text(json.dumps(build_record(date), indent=2) + "\n", encoding="utf-8", newline="\n")
    print(f"Wrote {RECORD.relative_to(ROOT).as_posix()}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
