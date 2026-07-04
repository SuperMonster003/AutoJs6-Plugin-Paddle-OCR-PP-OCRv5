#!/usr/bin/env python3

import argparse
import shutil
import tarfile
import urllib.request
from pathlib import Path
from typing import Optional

BOS_BASE = "https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0"

PROFILES = {
    "mobile": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-mobile",
    },
    "server": {
        "det": "PP-OCRv5_server_det",
        "rec": "PP-OCRv5_server_rec",
        "asset": "ppocrv5-server",
    },
    "english": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "en_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-english",
    },
    "korean": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "korean_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-korean",
    },
    "latin": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "latin_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-latin",
    },
    "eslav": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "eslav_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-eslav",
    },
    "thai": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "th_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-thai",
    },
    "greek": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "el_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-greek",
    },
    "arabic": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "arabic_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-arabic",
    },
    "cyrillic": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "cyrillic_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-cyrillic",
    },
    "devanagari": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "devanagari_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-devanagari",
    },
    "telugu": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "te_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-telugu",
    },
    "tamil": {
        "det": "PP-OCRv5_mobile_det",
        "rec": "ta_PP-OCRv5_mobile_rec",
        "asset": "ppocrv5-tamil",
    },
}

SHARED_MOBILE_DET_ASSET = "ppocrv5-mobile-det"


def model_url(model_name: str) -> str:
    return f"{BOS_BASE}/{model_name}_onnx_infer.tar"


def download(url: str, target: Path) -> None:
    target.parent.mkdir(parents=True, exist_ok=True)
    if target.exists() and target.stat().st_size > 0:
        print(f"skip: {target}")
        return
    print(f"download: {url}")
    urllib.request.urlretrieve(url, target)


def resolve_local_model_tar(source_dir: Optional[Path], model_name: str) -> Optional[Path]:
    if source_dir is None:
        return None
    candidates = [
        source_dir / f"{model_name}_onnx_infer.tar",
        source_dir / f"{model_name}_infer.tar",
        source_dir / f"{model_name}.tar",
    ]
    return next((candidate for candidate in candidates if candidate.is_file()), None)


def safe_extract_tar(tar_path: Path, out_dir: Path) -> Path:
    out_dir.mkdir(parents=True, exist_ok=True)
    base = out_dir.resolve()
    with tarfile.open(tar_path) as archive:
        for member in archive.getmembers():
            target = (out_dir / member.name).resolve()
            if base not in target.parents and target != base:
                raise RuntimeError(f"unsafe tar member: {member.name}")
        try:
            archive.extractall(out_dir, filter="data")
        except TypeError:
            archive.extractall(out_dir)

    children = [p for p in out_dir.iterdir() if p.is_dir()]
    return children[0] if len(children) == 1 else out_dir


def copy_onnx_model(extracted_dir: Path, dst_dir: Path) -> None:
    dst_dir.mkdir(parents=True, exist_ok=True)
    onnx = next(extracted_dir.rglob("inference.onnx"), None)
    yml = next(extracted_dir.rglob("inference.yml"), None)
    if onnx is None:
        raise FileNotFoundError(f"inference.onnx not found under {extracted_dir}")
    shutil.copy2(onnx, dst_dir / "inference.onnx")
    if yml is not None:
        shutil.copy2(yml, dst_dir / "inference.yml")


def fetch_model(model_name: str, cache_dir: Path, source_dir: Optional[Path]) -> Path:
    tar_path = cache_dir / f"{model_name}_onnx_infer.tar"
    extract_dir = cache_dir / model_name
    local_tar = resolve_local_model_tar(source_dir, model_name)
    if local_tar is not None:
        tar_path.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(local_tar, tar_path)
    else:
        download(model_url(model_name), tar_path)
    return safe_extract_tar(tar_path, extract_dir)


def profile_assets_root(profile: str, overridden_assets_root: Optional[str]) -> Path:
    if overridden_assets_root:
        return Path(overridden_assets_root)
    return Path("app") / "src" / profile / "assets"


def prepare_profile(profile: str, cache_dir: Path, source_dir: Optional[Path], assets_root_override: Optional[str]) -> None:
    config = PROFILES[profile]
    assets_root = profile_assets_root(profile, assets_root_override)
    asset_name = config["asset"]

    det_dir = fetch_model(config["det"], cache_dir, source_dir)
    if assets_root_override is None and config["det"] == "PP-OCRv5_mobile_det":
        copy_onnx_model(det_dir, Path("app") / "src" / "sharedMobileDet" / "assets" / "models" / SHARED_MOBILE_DET_ASSET / "det")
    else:
        copy_onnx_model(det_dir, assets_root / "models" / asset_name / "det")

    rec_dir = fetch_model(config["rec"], cache_dir, source_dir)
    copy_onnx_model(rec_dir, assets_root / "models" / asset_name / "rec")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--profile", choices=[*PROFILES.keys(), "all"], default="mobile")
    parser.add_argument("--assets-root", default=None)
    parser.add_argument("--cache-dir", default=".cache/ppocrv5")
    parser.add_argument("--source-dir", default=None)
    args = parser.parse_args()

    cache_dir = Path(args.cache_dir)
    source_dir = Path(args.source_dir) if args.source_dir else None
    profiles = list(PROFILES.keys()) if args.profile == "all" else [args.profile]
    for profile in profiles:
        prepare_profile(profile, cache_dir, source_dir, args.assets_root)


if __name__ == "__main__":
    main()
