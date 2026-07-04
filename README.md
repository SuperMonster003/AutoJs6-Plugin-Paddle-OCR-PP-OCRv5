# AutoJs6 Paddle OCR PP-OCRv5 Plugin

This plugin is implemented with the PaddleOCR Android ONNX Runtime SDK path.
It does not depend on the old local Paddle Lite C++ build chain.

## Profiles

- `mobile`: default profile. Uses `PP-OCRv5_mobile_det` and `PP-OCRv5_mobile_rec`.
- `server`: high-accuracy profile. Uses `PP-OCRv5_server_det` and `PP-OCRv5_server_rec`.
- `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu`, `tamil`: share `PP-OCRv5_mobile_det` from main assets and use the matching official PP-OCRv5 language recognition model.

The `mobile` flavor keeps the legacy plugin id:

```text
paddle-ocr-pp-ocrv5
```

Other flavors use ids such as `paddle-ocr-pp-ocrv5-server` and `paddle-ocr-pp-ocrv5-latin`.

## Prepare Models

```bash
python scripts\prepare_ppocrv5_assets.py --profile all
```

The script downloads official ONNX tar packages from BOS:

```text
https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
```

## Build

```bash
./gradlew :app:assembleMobileDebug
./gradlew :app:assembleServerDebug
```

## Install

```bash
./gradlew :app:installMobileDebug
./gradlew :app:installServerDebug
```

Build all profiles:

```powershell
.\gradlew.bat :app:assembleDebug
```
