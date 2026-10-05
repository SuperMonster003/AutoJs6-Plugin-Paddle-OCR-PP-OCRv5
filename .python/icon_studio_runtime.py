"""Portable icon recipe renderer (MPL-2.0).

Geometry/PNG encoding derive from AutoJs6 Optical geometry v1. This file is also
copied into managed repositories: only Python's standard library and Pillow are
required there. Preview and repository generation must use this exact code.
"""

from __future__ import annotations

import argparse
import hashlib
import io
import json
import math
import re
import struct
import xml.etree.ElementTree as ET
import zlib
from pathlib import Path

from PIL import Image, ImageChops, ImageDraw, ImageOps

VERSION = "1.3.0"
SUPPORTED_VERSIONS = {"1.0.0", "1.1.0", "1.2.0", VERSION}
APPLICATION_MANIFEST = "app/src/main/AndroidManifest.xml"
APPLICATION_ICON = "ic_icon_studio_application"
APPLICATION_OUTPUTS = {
    "app/src/main/res/mipmap/ic_icon_studio_application.png": "legacy-day",
    "app/src/main/res/mipmap-night/ic_icon_studio_application.png": "legacy-night",
    "app/src/main/res/mipmap/ic_icon_studio_application_foreground.png": "adaptive-day",
    "app/src/main/res/mipmap-night/ic_icon_studio_application_foreground.png": "adaptive-night",
    "app/src/main/res/mipmap/ic_icon_studio_application_monochrome.png": "mono",
    "app/src/main/res/mipmap-anydpi-v26/ic_icon_studio_application.xml": "application-adaptive",
    "app/src/main/res/mipmap-night-anydpi-v26/ic_icon_studio_application.xml": "application-adaptive",
    "app/src/main/res/raw/keep_icon_studio_application.xml": "application-keep",
}


def application_outputs(min_sdk: int = 24) -> dict[str, str]:
    return {
        path.replace("anydpi-v26/", "anydpi/") if min_sdk >= 26 else path: role
        for path, role in APPLICATION_OUTPUTS.items()
        if min_sdk < 26 or not role.startswith("legacy-")
    }
RECOMMENDED_SCALE = (0.80, 1.20)
VISUAL_TARGET = 0.52
RASTER_TOLERANCE = 0.006
THREE_BACKGROUNDS = {"day": "#fafafa", "night": "#212121"}
SIZE = 432
SUPERSAMPLE = 4
ASSET_ID = re.compile(r"[a-f0-9]{64}\Z")
KEEP_RESOURCE = b"""<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools"
    tools:keep="@mipmap/ic_plugin_center" />
"""


def sha(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def canonical(value) -> bytes:
    return json.dumps(
        value,
        ensure_ascii=False,
        sort_keys=True,
        separators=(",", ":"),
        allow_nan=False,
    ).encode()


def confined(root: Path, relative: str) -> Path:
    if (
        not isinstance(relative, str)
        or not relative
        or "\\" in relative
        or ":" in relative
    ):
        raise ValueError("无效的项目相对路径")
    part = Path(relative)
    if part.is_absolute() or ".." in part.parts:
        raise ValueError("文件路径不能越出项目目录")
    result = (root / part).resolve()
    if not result.is_relative_to(root.resolve()):
        raise ValueError("文件路径通过链接越出了项目目录")
    return result


def encode_png(image: Image.Image) -> bytes:
    rgba = image.convert("RGBA")
    pixels = rgba.tobytes()
    stride = rgba.width * 4
    rows = b"".join(
        b"\x00" + pixels[i : i + stride] for i in range(0, len(pixels), stride)
    )
    compressor = zlib.compressobj(level=9, strategy=zlib.Z_FIXED)
    compressed = compressor.compress(rows) + compressor.flush()

    def chunk(kind, data):
        return (
            struct.pack(">I", len(data))
            + kind
            + data
            + struct.pack(">I", zlib.crc32(kind + data))
        )

    return (
        b"\x89PNG\r\n\x1a\n"
        + chunk(
            b"IHDR", struct.pack(">IIBBBBB", rgba.width, rgba.height, 8, 6, 0, 0, 0)
        )
        + chunk(b"IDAT", compressed)
        + chunk(b"IEND", b"")
    )


def load_image(data: bytes) -> Image.Image:
    with Image.open(io.BytesIO(data)) as image:
        if image.format not in {"PNG", "JPEG", "WEBP"}:
            raise ValueError("仅支持 PNG、JPEG、WebP 或经导入转换后的 SVG")
        if getattr(image, "n_frames", 1) != 1:
            raise ValueError("请选择静态图稿，动画请先导出所需的一帧")
        if image.width * image.height > 32_000_000 or max(image.size) > 8192:
            raise ValueError(
                "图片过大，请使用边长不超过 8192 像素、总像素不超过 3200 万的源图"
            )
        return ImageOps.exif_transpose(image).convert("RGBA")


def visual_size(alpha: Image.Image) -> float:
    bounds = alpha.point(lambda a: 255 if a >= 16 else 0).getbbox()
    if not bounds:
        return 0.0
    area = (bounds[2] - bounds[0]) * (bounds[3] - bounds[1])
    ink = sum(value * count for value, count in enumerate(alpha.histogram())) / 255
    return math.sqrt((area + ink) / 2) / alpha.width


def maximum_radius(alpha: Image.Image) -> float:
    cx, cy = (alpha.width - 1) / 2, (alpha.height - 1) / 2
    raw = alpha.tobytes()
    maximum = 0.0
    for y in range(alpha.height):
        row = raw[y * alpha.width : (y + 1) * alpha.width]
        occupied = [x for x, value in enumerate(row) if value]
        if occupied:
            maximum = max(
                maximum,
                math.hypot(occupied[0] - cx, y - cy),
                math.hypot(occupied[-1] - cx, y - cy),
            )
    return maximum


def default_tone(mode="mask", foreground="#272727"):
    return {
        "mode": mode,
        "foreground": foreground,
        "brightness": 1.0,
        "contrast": 1.0,
        "gamma": 1.0,
        "threshold": 128,
        "invert": False,
    }


def is_three(recipe: dict) -> bool:
    return recipe.get("repository", "").startswith("AutoJs6-Plugin-Three-")


def backgrounds(recipe: dict) -> dict:
    if is_three(recipe):
        return dict(THREE_BACKGROUNDS)
    return recipe["params"].get("backgrounds", dict(THREE_BACKGROUNDS))


def rgba_color(value: str) -> tuple:
    return (
        (0, 0, 0, 0)
        if value == "transparent"
        else tuple(bytes.fromhex(value[1:])) + (255,)
    )


def validate_params(params: dict) -> None:
    expected = {"geometry", "day", "night", "sources"}
    if not isinstance(params, dict) or set(params) - {"backgrounds"} != expected:
        raise ValueError("图标参数结构不完整或包含未知字段")
    if "backgrounds" in params:
        colors = params["backgrounds"]
        if not isinstance(colors, dict) or set(colors) != {"day", "night"}:
            raise ValueError("背景参数须分别包含亮色和暗色")
        if any(
            not isinstance(c, str)
            or not re.fullmatch(r"transparent|#[0-9a-fA-F]{6}", c)
            for c in colors.values()
        ):
            raise ValueError("背景须使用 #RRGGBB 或 transparent")
    geometry = params["geometry"]
    if (
        not isinstance(geometry, dict)
        or set(geometry) != {"mode", "scale", "offsetX", "offsetY"}
        or not isinstance(geometry["mode"], str)
        or geometry["mode"] not in {"normalized", "preserve"}
    ):
        raise ValueError("几何参数无效")
    for key, low, high in (
        ("scale", 0.25, 2),
        ("offsetX", -0.5, 0.5),
        ("offsetY", -0.5, 0.5),
    ):
        value = geometry[key]
        if (
            isinstance(value, bool)
            or not isinstance(value, (int, float))
            or not math.isfinite(value)
            or not low <= value <= high
        ):
            raise ValueError(f"{key} 超出允许范围")
    for mode in ("day", "night"):
        tone = params[mode]
        if (
            not isinstance(tone, dict)
            or set(tone) != set(default_tone())
            or not isinstance(tone["mode"], str)
            or tone["mode"] not in {"mask", "grayscale", "original", "binary"}
        ):
            raise ValueError("色调模式无效")
        if not isinstance(tone["foreground"], str) or not re.fullmatch(
            r"#[0-9a-fA-F]{6}", tone["foreground"]
        ):
            raise ValueError("前景颜色须使用 #RRGGBB 格式")
        for key, low, high in (
            ("brightness", 0.1, 3),
            ("contrast", 0, 3),
            ("gamma", 0.2, 3),
            ("threshold", 0, 255),
        ):
            value = tone[key]
            if (
                isinstance(value, bool)
                or not isinstance(value, (int, float))
                or not math.isfinite(value)
                or not low <= value <= high
            ):
                raise ValueError(f"{key} 超出允许范围")
        if not isinstance(tone["invert"], bool):
            raise ValueError("反色参数无效")
    if (
        not isinstance(params["sources"], dict)
        or set(params["sources"]) != {"day", "night", "alphaMode"}
        or not isinstance(params["sources"]["alphaMode"], str)
        or params["sources"]["alphaMode"] not in {"alpha", "dark-ink"}
    ):
        raise ValueError("源图参数无效")
    for mode in ("day", "night"):
        if not isinstance(params["sources"][mode], str) or not ASSET_ID.fullmatch(
            params["sources"][mode]
        ):
            raise ValueError("源图标识无效")


def source_alpha(image: Image.Image, mode="alpha") -> Image.Image:
    alpha = image.getchannel("A")
    if mode == "dark-ink":
        alpha = ImageChops.multiply(alpha, ImageOps.invert(image.convert("L")))
    return alpha


def apply_tone(image: Image.Image, tone: dict) -> Image.Image:
    alpha = image.getchannel("A")
    mode = tone["mode"]
    if mode == "mask":
        color = tuple(int(tone["foreground"][i : i + 2], 16) for i in (1, 3, 5))
        result = Image.new("RGB", image.size, color)
    elif mode in {"grayscale", "binary"}:
        result = ImageOps.grayscale(image).convert("RGB")
    else:
        result = image.convert("RGB")
    if (
        tone["brightness"] != 1
        or tone["contrast"] != 1
        or tone["gamma"] != 1
        or tone["invert"]
        or mode == "binary"
    ):
        table = []
        for value in range(256):
            value = max(
                0.0,
                min(
                    1.0,
                    ((value / 255 - 0.5) * tone["contrast"] + 0.5) * tone["brightness"],
                ),
            )
            value = value ** (1 / tone["gamma"])
            value = 1 - value if tone["invert"] else value
            value = round(value * 255)
            table.append(
                (255 if value >= tone["threshold"] else 0)
                if mode == "binary"
                else value
            )
        result = result.point(table * 3)
    result = result.convert("RGBA")
    result.putalpha(alpha)
    return result


def glyph(alpha, color):
    result = Image.new("RGBA", alpha.size, (*color[:3], 255))
    result.putalpha(alpha)
    return result


def place(alpha: Image.Image, ratio: float, x: float, y: float) -> Image.Image:
    side = SIZE * SUPERSAMPLE
    width = max(1, round(side * ratio))
    height = max(1, round(width * alpha.height / alpha.width))
    left = round((side - width) / 2 + x * width)
    top = round((side - height) / 2 + y * height)
    if left < 0 or top < 0 or left + width > side or top + height > side:
        raise ValueError("图案超出画布，请减小尺寸或偏移")
    canvas = Image.new("L", (side, side))
    canvas.paste(alpha.resize((width, height), Image.Resampling.LANCZOS), (left, top))
    return canvas.resize((SIZE, SIZE), Image.Resampling.LANCZOS)


def legacy(foreground: Image.Image, color) -> Image.Image:
    side = SIZE * SUPERSAMPLE
    alpha = Image.new("L", (side, side))
    ImageDraw.Draw(alpha).ellipse((0, 0, side - 1, side - 1), fill=255)
    background = glyph(alpha.resize((SIZE, SIZE), Image.Resampling.LANCZOS), color)
    return Image.alpha_composite(background, foreground)


def render_images(recipe: dict, loader) -> dict[str, Image.Image]:
    params = recipe["params"]
    validate_params(params)
    geometry = params["geometry"]
    sources = {
        mode: load_image(loader(params["sources"][mode])) for mode in ("day", "night")
    }
    alpha = source_alpha(sources["day"], params["sources"]["alphaMode"])
    bounds = alpha.getbbox()
    if bounds is None:
        raise ValueError("源图没有可见内容")
    raw_alpha = alpha.crop(bounds)
    normalized = geometry["mode"] == "normalized"
    ratio = (
        VISUAL_TARGET * geometry["scale"] / visual_size(raw_alpha)
        if normalized
        else geometry["scale"]
    )
    result = {}
    for mode in ("day", "night"):
        original = sources[mode]
        own_alpha = source_alpha(original, params["sources"]["alphaMode"])
        own_bounds = own_alpha.getbbox()
        if not own_bounds:
            raise ValueError("亮暗源图都必须包含可见内容")
        if normalized:
            original = original.crop(own_bounds)
        else:
            # Preserve the original composition on a square artboard, including its margins.
            width, height = original.size
            square = Image.new("RGBA", (max(width, height),) * 2)
            square.alpha_composite(
                original, ((square.width - width) // 2, (square.height - height) // 2)
            )
            original = square
        if normalized:
            crop_alpha = own_alpha.crop(own_bounds)
        else:
            crop_alpha = original.getchannel("A")
        for surface, factor in (("ui", 1.0), ("adaptive", 72 / 108)):
            target_ratio = (
                VISUAL_TARGET * geometry["scale"] / visual_size(crop_alpha)
                if normalized and recipe["profile"] != "neutral-v1"
                else ratio
            ) * factor
            placed = place(
                crop_alpha, target_ratio, geometry["offsetX"], geometry["offsetY"]
            )
            if params[mode]["mode"] == "mask":
                toned = apply_tone(glyph(placed, (255, 255, 255)), params[mode])
            else:
                original_toned = apply_tone(original, params[mode])
                side = SIZE * SUPERSAMPLE
                width = max(1, round(side * target_ratio))
                height = max(1, round(width * crop_alpha.height / crop_alpha.width))
                left = round((side - width) / 2 + geometry["offsetX"] * width)
                top = round((side - height) / 2 + geometry["offsetY"] * height)
                canvas = Image.new("RGBA", (side, side))
                canvas.alpha_composite(
                    original_toned.resize((width, height), Image.Resampling.LANCZOS),
                    (left, top),
                )
                toned = canvas.resize((SIZE, SIZE), Image.Resampling.LANCZOS)
                toned.putalpha(placed)
            result[f"{surface}-{mode}"] = toned
    result["mono"] = glyph(result["adaptive-day"].getchannel("A"), (0, 0, 0))
    for mode, color in backgrounds(recipe).items():
        result[f"legacy-{mode}"] = (
            result[f"ui-{mode}"].copy()
            if color == "transparent"
            else legacy(result[f"ui-{mode}"], rgba_color(color)[:3])
        )
    return result


def inspect_images(recipe: dict, images: dict) -> dict:
    errors, warnings = [], []
    metrics = {}
    standard = recipe["profile"] == "neutral-v1"
    neutral = is_three(recipe) or (standard and not recipe.get("repository"))
    if (
        is_three(recipe)
        and recipe["params"].get("backgrounds", THREE_BACKGROUNDS) != THREE_BACKGROUNDS
    ):
        errors.append("Three 系列固定使用 #FAFAFA / #212121 背景")
    if standard:
        geometry = recipe["params"]["geometry"]
        if geometry["mode"] != "normalized":
            errors.append("规范图标必须使用统一的视觉归一化方式")
    alphas = []
    for mode in ("day", "night"):
        image = images[f"ui-{mode}"]
        alpha = image.getchannel("A")
        alphas.append(alpha.tobytes())
        pixels = image.get_flattened_data()
        colored = sum(1 for r, g, b, a in pixels if a > 0 and (r != g or g != b))
        opaque = [r for r, g, b, a in pixels if a == 255]
        mean = sum(opaque) / len(opaque) if opaque else None
        value = visual_size(alpha)
        bounds = alpha.point(lambda a: 255 if a >= 16 else 0).getbbox()
        radius = maximum_radius(alpha)
        adaptive_radius = maximum_radius(images[f"adaptive-{mode}"].getchannel("A"))
        metrics[mode] = {
            "visualSize": value,
            "bounds": bounds,
            "transparentRatio": alpha.histogram()[0] / (SIZE * SIZE),
            "radius": radius,
            "adaptiveRadius": adaptive_radius,
            "coloredPixels": colored,
        }
        if neutral:
            if mean is None:
                errors.append("图稿需要包含清晰的不透明主体")
            elif (mode == "day" and mean >= 128) or (mode == "night" and mean <= 128):
                errors.append(
                    "浅色图稿的主体应为深色，深色图稿的主体应为浅色；请调整前景、亮度或反色"
                )
            if colored:
                errors.append(
                    f"{'浅色' if mode == 'day' else '深色'}图稿包含彩色像素，请选择灰阶或单色填充"
                )
        if standard:
            if alpha.histogram()[0] <= SIZE * SIZE / 2:
                errors.append("透明区域须超过画布的一半")
            # Optical size is an art-direction guide. Cropping and safe circles
            # are independent constraints and remain blocking errors below.
            low, high = RECOMMENDED_SCALE
            if not (low <= geometry["scale"] <= high) or not (
                VISUAL_TARGET * low - RASTER_TOLERANCE
                <= value
                <= VISUAL_TARGET * high + RASTER_TOLERANCE
            ):
                warnings.append(
                    "尺寸偏离建议范围（缩放 80%–120%，视觉尺寸约 0.42–0.62）；可继续应用，请结合整组预览判断"
                )
            if (
                bounds
                and max(bounds[2] - bounds[0], bounds[3] - bounds[1]) > SIZE * 0.80
            ):
                warnings.append(
                    "图案最长边超过画布的 80%；可继续应用，请留意圆形边缘的视觉留白"
                )
            if radius > 216:
                errors.append("图案超出列表圆形边界")
            if recipe.get("launcher") and adaptive_radius > 132:
                errors.append("启动器前景超出 33 dp 安全圆")
        elif value > 0.8:
            warnings.append("当前图稿较满，可使用“按标准归一化”与其他插件比较")
    if standard and alphas[0] != alphas[1]:
        errors.append("亮暗图稿的透明轮廓必须一致")
    return {
        "metrics": metrics,
        "errors": list(dict.fromkeys(errors)),
        "warnings": list(dict.fromkeys(warnings)),
    }


def generated_files(recipe: dict, loader, *, strict=True) -> dict[str, bytes]:
    images = render_images(recipe, loader)
    report = inspect_images(recipe, images)
    if strict and report["errors"]:
        raise ValueError("；".join(report["errors"]))
    baseline = recipe.get("baseline", {})
    artwork = lambda params: {
        k: v for k, v in (params or {}).items() if k != "backgrounds"
    }
    unchanged = canonical(artwork(recipe["params"])) == canonical(
        artwork(baseline.get("params"))
    )
    background_changed = recipe["params"].get("backgrounds") != baseline.get(
        "params", {}
    ).get("backgrounds")
    result = {}
    for path, descriptor in recipe["outputs"].items():
        if not path.startswith("app/src/") or "/res/" not in path:
            raise ValueError("生成目标必须是项目资源文件")
        role = descriptor["role"]
        if role == "application-keep":
            result[path] = b'<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="@mipmap/ic_launcher" />\n'
        elif role == "application-adaptive":
            result[path] = (
                '<?xml version="1.0" encoding="utf-8"?>\n'
                '<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
                '    <background android:drawable="@color/ic_plugin_center_background" />\n'
                f'    <foreground android:drawable="@mipmap/{APPLICATION_ICON}_foreground" />\n'
                f'    <monochrome android:drawable="@mipmap/{APPLICATION_ICON}_monochrome" />\n'
                '</adaptive-icon>\n'
            ).encode()
        elif role == "background-keep":
            result[path] = (
                b'<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="@color/ic_plugin_center_background" />\n'
            )
        elif role == "background-color":
            color = backgrounds(recipe)[descriptor["theme"]]
            value = "#00000000" if color == "transparent" else color.upper()
            result[path] = (
                '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n    <color name="ic_plugin_center_background">'
                + value
                + "</color>\n</resources>\n"
            ).encode()
        elif role == "adaptive-background":
            original = loader(descriptor["original"])
            if not background_changed:
                result[path] = original
            else:
                theme = descriptor.get("theme")
                value = backgrounds(recipe).get(theme) if theme else None
                value = (
                    "@android:color/transparent"
                    if value == "transparent"
                    else value or "@color/ic_plugin_center_background"
                )
                result[path] = re.sub(
                    rb'(<background\b[^>]*android:drawable=")[^"]*(")',
                    lambda m, value=value: m[1] + value.encode() + m[2],
                    original,
                )
        elif descriptor.get("original") and (
            role in {"keep", "keep-rule"}
            or (unchanged and not (background_changed and role.startswith("legacy-")))
        ):
            result[path] = loader(descriptor["original"])
        elif role == "keep":
            raise ValueError("缺少需要保留的原资源")
        elif role == "keep-rule":
            result[path] = KEEP_RESOURCE
        else:
            image = images[role]
            target = tuple(descriptor.get("size", (SIZE, SIZE)))
            if target != image.size:
                image = image.resize(target, Image.Resampling.LANCZOS)
            result[path] = encode_png(image)
    return result


def application_manifest(data: bytes) -> bytes:
    """Change only the application's two icon references; preserve all components."""
    text = data.decode("utf-8")
    document = ET.fromstring(text)
    if len(document.findall("application")) != 1:
        raise ValueError("系统图标需要唯一的 application 节点")
    if not re.search(r'xmlns:android\s*=\s*[\"\']http://schemas.android.com/apk/res/android[\"\']', text):
        raise ValueError("无法识别 Manifest 的 android 命名空间")
    tags = list(re.finditer(r"<application(?=[\s/>])(?:[^\"'>]|\"[^\"]*\"|'[^']*')*?>", text))
    if len(tags) != 1:
        raise ValueError("无法安全定位 application 图标引用")
    match = tags[0]
    tag = match[0]
    for attribute in ("icon", "roundIcon"):
        pattern = rf"(\bandroid:{attribute}\s*=\s*)([\"'])(.*?)(\2)"
        value = "@mipmap/" + APPLICATION_ICON
        if re.search(pattern, tag, re.DOTALL):
            tag = re.sub(pattern, lambda m, value=value: m[1] + m[2] + value + m[4], tag, flags=re.DOTALL)
        else:
            closing = "/>" if tag.endswith("/>") else ">"
            head = tag[: -len(closing)]
            trimmed = head.rstrip()
            indentation = re.search(r"\n([ \t]+)\S", tag)
            separator = ("\r\n" if "\r\n" in tag else "\n") + indentation[1] if indentation else " "
            tag = trimmed + separator + f'android:{attribute}="{value}"' + head[len(trimmed) :] + closing
    return (text[: match.start()] + tag + text[match.end() :]).encode("utf-8")


def project_files(recipe: dict, loader, read, *, strict=True) -> dict[str, bytes | None]:
    outputs = generated_files(recipe, loader, strict=strict)
    if recipe.get("systemIcon"):
        outputs[APPLICATION_MANIFEST] = application_manifest(read(APPLICATION_MANIFEST))
        owned_paths = set(APPLICATION_OUTPUTS) | set(application_outputs(26))
        for path in recipe.get("systemIconObsolete", []):
            if path not in owned_paths or path in outputs:
                raise ValueError("不能移除不属于旧系统图标配置的文件: " + str(path))
            outputs[path] = None
        try:
            properties = read("version.properties")
        except FileNotFoundError:
            properties = None
        minimum = re.search(rb"(?m)^MIN_SDK_VERSION=(\d+)", properties or b"")
        if minimum and "systemIconMinSdk" in recipe and int(minimum[1]) != recipe["systemIconMinSdk"]:
            raise ValueError("最低 Android 版本已改变，请重新扫描并应用系统图标资源")
    return outputs


def remove_system_resource(root: Path, relative: str) -> None:
    if relative not in set(APPLICATION_OUTPUTS) | set(application_outputs(26)):
        raise ValueError("只能移除已列明的系统图标资源")
    path = confined(root, relative)
    if not path.is_relative_to((root / "app/src/main/res").resolve()):
        raise ValueError("系统图标删除目标越出资源目录")
    path.unlink()
    try:
        path.parent.rmdir()  # Only removes an empty resource folder; never recursive.
    except OSError:
        pass


def main(root: Path | None = None) -> int:
    parser = argparse.ArgumentParser(
        description="Generate resources from the committed Icon Studio recipe"
    )
    parser.add_argument("--check", action="store_true")
    parser.add_argument("--root", type=Path)
    args = parser.parse_args()
    root = (args.root or root or Path(__file__).resolve().parents[1]).resolve()
    recipe = json.loads((root / ".icons/recipe.json").read_text(encoding="utf-8"))
    if (
        recipe.get("schemaVersion") != 1
        or recipe.get("generatorVersion") not in SUPPORTED_VERSIONS
    ):
        raise ValueError("图标配方与生成内核版本不匹配")

    def loader(asset_id):
        if not ASSET_ID.fullmatch(asset_id):
            raise ValueError("源图标识无效")
        data = confined(root, f".icons/assets/{asset_id}").read_bytes()
        if sha(data) != asset_id:
            raise ValueError("源图摘要不匹配")
        return data

    outputs = project_files(recipe, loader, lambda path: confined(root, path).read_bytes())
    if any(confined(root, p).exists() and not confined(root, p).is_file() for p in outputs):
        raise ValueError("生成目标被同名目录占用")
    changed = [
        (confined(root, p), data)
        for p, data in outputs.items()
        if (confined(root, p).read_bytes() if confined(root, p).is_file() else None) != data
    ]
    if args.check:
        if changed:
            raise SystemExit(
                "图标资源与配方不一致: "
                + ", ".join(str(p.relative_to(root)) for p, _ in changed)
            )
        print(f"Verified {len(outputs)} Icon Studio resources")
        return 0
    for path, data in changed:
        if data is None:
            remove_system_resource(root, path.relative_to(root).as_posix())
            print(f"Removed obsolete system resource {path.relative_to(root).as_posix()}")
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
            print(f"Generated {path.relative_to(root).as_posix()}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
