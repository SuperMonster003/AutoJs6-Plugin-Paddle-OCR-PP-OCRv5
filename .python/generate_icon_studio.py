"""Regenerate icon assets from .icons/recipe.json."""
from pathlib import Path
from icon_studio_runtime import main

if __name__ == "__main__":
    raise SystemExit(main(Path(__file__).resolve().parents[1]))
