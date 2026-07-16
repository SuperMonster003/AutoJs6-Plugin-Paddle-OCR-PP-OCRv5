# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path


LANGUAGE_CODES = [
    "zh-Hans",
    "zh-Hant-HK",
    "zh-Hant-TW",
    "en",
    "fr",
    "es",
    "ja",
    "ko",
    "ru",
    "ar",
]
LANGUAGE_CODE_DEFAULT = "zh-Hans"
README_COMPAT_ROOT_LANGUAGES = set()
ANDROID_CHANGELOG_ALIASES = {
    "zh-Hans": ["zh", "zh-Hans"],
    "zh-Hant-HK": ["zh-rHK", "zh-Hant-HK"],
    "zh-Hant-TW": ["zh-rTW", "zh-Hant-TW"],
}


def project_root() -> Path:
    return Path(__file__).resolve().parents[1]


ROOT = project_root()
README_DIR = ROOT / ".readme"
CHANGELOG_DIR = ROOT / ".changelog"
ANDROID_CHANGELOG_DIR = ROOT / "app" / "src" / "main" / "assets" / "doc"


def load_json(path: Path):
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def load_properties(path: Path):
    properties = {}
    with path.open("r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#") or "=" not in line:
                continue
            key, value = line.split("=", 1)
            properties[key.strip()] = value.strip()
    return properties


def version_tag(version_name: str) -> str:
    return version_name if version_name.startswith("v") else f"v{version_name}"


def render_template(text: str, values: dict) -> str:
    def repl(match):
        key = match.group(1).strip()
        if key not in values:
            raise KeyError(f"Missing template value: {key}")
        return str(values[key])

    return re.sub(r"\{\{\s*([A-Za-z0-9_$.-]+)\s*\}\}", repl, text)


def render_dynamic(value, values: dict):
    if isinstance(value, dict):
        return {k: render_dynamic(v, values) for k, v in value.items()}
    if isinstance(value, list):
        return [render_dynamic(v, values) for v in value]
    if isinstance(value, str):
        return render_template(value, values)
    return value


def bullet_list(items):
    return "\n".join(f"- {item}" for item in items)


def markdown_link(label, url):
    return f"[{label}]({url})"


def load_languages():
    common = load_json(README_DIR / "common.json")
    version_properties = load_properties(ROOT / "version.properties")
    common = {
        **common,
        "version_name": version_properties["VERSION_NAME"],
        "version_name_tag": version_tag(version_properties["VERSION_NAME"]),
        "min_sdk_version": version_properties["MIN_SDK_VERSION"],
        "target_sdk_version": version_properties["TARGET_SDK_VERSION"],
    }
    languages = {}
    changelogs = {}
    for code in LANGUAGE_CODES:
        raw_lang = load_json(README_DIR / f"lang_{code}.json")
        merged_lang = {**common, **raw_lang}
        languages[code] = render_dynamic(merged_lang, merged_lang)

        raw_changelog = load_json(CHANGELOG_DIR / f"lang_{code}.json")
        changelog_values = {k: v for k, v in raw_changelog.items() if k != "$data"}
        changelog_values = render_dynamic(changelog_values, changelog_values)
        changelog_data = render_dynamic(raw_changelog["$data"], changelog_values)
        changelogs[code] = {
            "values": changelog_values,
            "data": changelog_data,
        }
    validate_changelog_version(languages, changelogs)
    return languages, changelogs


def validate_changelog_version(languages, changelogs):
    expected = languages[LANGUAGE_CODE_DEFAULT]["version_name_tag"]
    latest = next(iter(changelogs[LANGUAGE_CODE_DEFAULT]["data"].keys()))
    if latest != expected:
        raise ValueError(f"Latest changelog version {latest!r} does not match {expected!r}")


def format_changelog_items(changelog, limit=None):
    values = changelog["values"]
    data = changelog["data"]
    chunks = []
    for index, (version_name, item) in enumerate(data.items()):
        if limit is not None and index >= limit:
            break
        lines = [
            f"# {version_name}",
            "",
            f"###### {item['released_date']}",
            "",
        ]
        for category in ["hint", "feature", "fix", "improvement", "dependency"]:
            for text in item.get(category, []):
                label = values[f"changelog_label_{category}"]
                lines.append(f"* `{label}` {text}")
        chunks.append("\n".join(lines).rstrip())
    return "\n\n".join(chunks).rstrip() + "\n"


def build_language_list(target_code, languages):
    repo_url = languages[target_code]["repo_url"]
    lines = []
    for code in LANGUAGE_CODES:
        content = languages[code]
        label = f"{content['$name']} [{code}]"
        if code == target_code:
            lines.append(f"- {label} # {content['text_current_lowercase']}")
        else:
            url = f"{repo_url}/blob/master/.readme/README-{code}.md"
            lines.append(f"- {markdown_link(label, url)}")
    return "\n".join(lines)


def build_readme_values(code, languages, changelogs):
    content = dict(languages[code])
    content["placeholder_ul_languages_all_supported"] = build_language_list(code, languages)
    content["placeholder_features"] = bullet_list(content["features"])
    content["placeholder_profiles"] = bullet_list(content["profiles"])
    content["placeholder_prepare_notes"] = bullet_list(content["prepare_notes"])
    content["placeholder_latest_release_history"] = format_changelog_items(changelogs[code], limit=3).rstrip()
    content["placeholder_read_more_in_changelog_md"] = markdown_link(
        "CHANGELOG.md",
        f"{content['repo_url']}/blob/master/.changelog/CHANGELOG-{code}.md",
    )
    return content


def write_text(path: Path, text: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text, encoding="utf-8", newline="\n")
    print(f"Generated {path.relative_to(ROOT)}")


def generate_readmes(languages, changelogs):
    template = (README_DIR / "template_readme.md").read_text(encoding="utf-8")
    for code in LANGUAGE_CODES:
        output = render_template(template, build_readme_values(code, languages, changelogs))
        path = README_DIR / f"README-{code}.md"
        write_text(path, output)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ROOT / "README.md", output)
        if code in README_COMPAT_ROOT_LANGUAGES:
            write_text(ROOT / f"README-{code}.md", output)


def generate_changelogs(languages, changelogs):
    template = (CHANGELOG_DIR / "template_changelog.md").read_text(encoding="utf-8")
    for code in LANGUAGE_CODES:
        values = dict(languages[code])
        values["placeholder_release_history"] = format_changelog_items(changelogs[code]).rstrip()
        output = render_template(template, values)
        write_text(CHANGELOG_DIR / f"CHANGELOG-{code}.md", output)

        latest_only = format_changelog_items(changelogs[code], limit=1)
        names = ANDROID_CHANGELOG_ALIASES.get(code, [code])
        for name in names:
            write_text(ANDROID_CHANGELOG_DIR / f"CHANGELOG-{name}.md", latest_only)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ANDROID_CHANGELOG_DIR / "CHANGELOG.md", latest_only)


def main():
    if LANGUAGE_CODE_DEFAULT not in LANGUAGE_CODES:
        raise ValueError(f"Default language code {LANGUAGE_CODE_DEFAULT!r} is not in LANGUAGE_CODES")
    languages, changelogs = load_languages()
    generate_changelogs(languages, changelogs)
    generate_readmes(languages, changelogs)


if __name__ == "__main__":
    main()
