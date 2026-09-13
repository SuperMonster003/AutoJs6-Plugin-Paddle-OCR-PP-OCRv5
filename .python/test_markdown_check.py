import importlib.util
from pathlib import Path
import tempfile
import unittest

SPEC = importlib.util.spec_from_file_location("generator_under_test", Path(__file__).with_name("generate_markdown.py"))
generator = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(generator)

class MarkdownCheckTest(unittest.TestCase):
    def setUp(self):
        temporary = tempfile.TemporaryDirectory()
        self.addCleanup(temporary.cleanup)
        self.root = Path(temporary.name)
        generator.ROOT = self.root
        generator.CHECK_ONLY = True
        generator.DRIFT = []

    def test_check_does_not_create_missing_output(self):
        output = self.root / "nested/README.md"
        generator.write_text(output, "expected")
        self.assertFalse(output.parent.exists())
        self.assertEqual(["nested/README.md"], [value.replace("\\", "/") for value in generator.DRIFT])

    def test_check_does_not_overwrite_stale_output(self):
        output = self.root / "README.md"
        output.write_text("stale", encoding="utf-8")
        stamp = output.stat().st_mtime_ns
        generator.write_text(output, "expected")
        self.assertEqual("stale", output.read_text(encoding="utf-8"))
        self.assertEqual(stamp, output.stat().st_mtime_ns)
        self.assertEqual(["README.md"], generator.DRIFT)

    def test_matching_output_is_clean(self):
        output = self.root / "README.md"
        output.write_text("expected", encoding="utf-8")
        generator.write_text(output, "expected")
        self.assertEqual([], generator.DRIFT)

    def test_unknown_template_key_fails(self):
        with self.assertRaises(KeyError): generator.render_template("{{ absent }}", {})

if __name__ == "__main__": unittest.main()
