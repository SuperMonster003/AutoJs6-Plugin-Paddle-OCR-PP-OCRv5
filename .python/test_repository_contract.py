"""Source-level release contracts; device/Binder tests remain separate."""
from pathlib import Path
import json
import re
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
A = "{http://schemas.android.com/apk/res/android}"
LOCALES = ["values", "values-en", "values-ar", "values-es", "values-fr", "values-ja", "values-ko", "values-ru", "values-zh", "values-zh-rHK", "values-zh-rTW"]
LANGS = {"en", "ar", "es", "fr", "ja", "ko", "ru", "zh-Hans", "zh-Hant-HK", "zh-Hant-TW"}

class RepositoryContractTest(unittest.TestCase):
    def test_wake_contract_is_resolvable_and_permission_protected(self):
        manifest = ET.parse(ROOT / "app/src/main/AndroidManifest.xml").getroot()
        self.assertIn("org.autojs.permission.PLUGIN", [item.get(A+"name") for item in manifest.findall("uses-permission")])
        app = manifest.find("application")
        self.assertEqual("false", app.get(A+"allowBackup"))
        declaration = next(item for item in app.findall("meta-data") if item.get(A+"name") == "org.autojs.plugin.WAKE_ACTIVITY")
        target = declaration.get(A+"value")
        wake = next(item for item in app.findall("activity") if item.get(A+"name") == target)
        for key in ["exported", "excludeFromRecents", "finishOnTaskLaunch"]:
            self.assertEqual("true", wake.get(A+key))
        self.assertEqual("org.autojs.permission.PLUGIN", wake.get(A+"permission"))
        self.assertEqual("@android:style/Theme.NoDisplay", wake.get(A+"theme"))
        self.assertIn("org.autojs.plugin.action.WAKE", [item.get(A+"name") for item in wake.findall("intent-filter/action")])
        self.assertIn("android.intent.category.DEFAULT", [item.get(A+"name") for item in wake.findall("intent-filter/category")])
        source = list((ROOT/"app/src/main").rglob(target.rsplit(".",1)[-1]+".kt")) + list((ROOT/"app/src/main").rglob(target.rsplit(".",1)[-1]+".java"))
        self.assertTrue(source, "Wake class source must exist")
        self.assertIn("finish()", source[0].read_text(encoding="utf-8"))

    def test_localized_resources_are_complete_and_normalized(self):
        resources = ROOT / "app/src/main/res"
        mapping = {}
        for locale in LOCALES:
            path = resources / locale / "strings.xml"
            tree = ET.parse(path).getroot()
            strings = {item.get("name"): "".join(item.itertext()) for item in tree.findall("string")}
            self.assertIn("plugin_description", strings, locale)
            self.assertEqual(list(strings), sorted(strings), locale)
            self.assertEqual(len(tree), len(strings), locale)
            self.assertNotIn("app_name", strings, locale)
            self.assertFalse(re.search(r"[.,!?;:]$", strings["plugin_description"]), locale)
            self.assertFalse(re.search("[，。；：！？（）【】、…“”‘’]", path.read_text(encoding="utf-8")), locale)
            mapping[locale] = strings
        for name in mapping["values"].keys() & mapping["values-en"].keys():
            self.assertEqual(mapping["values"][name], mapping["values-en"][name], name)

    def test_changelog_language_set_and_current_version(self):
        sources = ROOT / ".changelog"
        self.assertEqual(LANGS, {path.stem[5:] for path in sources.glob("lang_*.json")})
        self.assertFalse(list(sources.glob("CHANGELOG*.md")))
        version = re.search(r"^VERSION_NAME=(\d+\.\d+\.\d+)", (ROOT/"version.properties").read_text(), re.M).group(1)
        for path in sources.glob("lang_*.json"):
            data = json.loads(path.read_text(encoding="utf-8"))
            self.assertIn("v"+version, data["$data"], path.name)
        self.assertIn("简体中文", (ROOT/"README.md").read_text(encoding="utf-8"))

    def test_icon_and_public_build_platform(self):
        self.assertTrue((ROOT/"app/src/main/res/mipmap/ic_launcher.png").read_bytes().startswith(b"\x89PNG\r\n\x1a\n"))
        settings = (ROOT/"settings.gradle.kts").read_text(encoding="utf-8")
        self.assertIn('version "1.8.1"', settings)
        self.assertNotIn("autojs.buildPlugins.includeBuild", settings)
        name = re.search(r'rootProject.name\s*=\s*"([^"]+)"', settings).group(1)
        self.assertEqual(name.lower(), name)


    def test_fixed_android_upgrade_offset(self):
        text = (ROOT / "version.properties").read_text(encoding="utf-8")
        build = int(re.search(r"^VERSION_BUILD=(\d+)$", text, re.M).group(1))
        offset = int(re.search(r"^VERSION_CODE_OFFSET=(\d+)$", text, re.M).group(1))
        self.assertEqual(49, offset, "The migration offset is fixed to preserve Android upgrade ordering")
        self.assertGreaterEqual(build + offset, 68)

if __name__ == "__main__": unittest.main()
