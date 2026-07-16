<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>Paddle OCR text recognition plugin based on PP-OCRv5</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/commit/96c9b45bc90d5ac5516e6bb1f10848c628418dac"><img alt="Created" src="https://img.shields.io/date/1773539058?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Languages

******

The current README.md supports the following languages:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- English [en] # current
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### Introduction

******

The AutoJs6 Paddle OCR PP-OCRv5 Plugin provides PaddleOCR ONNX Runtime based text detection and text recognition for AutoJs6. It supports the mobile default model, the high-accuracy server model, and multilingual recognition models.

******

### Features

******

- Provides the `paddle-ocr` plugin service with default plugin ID `paddle-ocr-pp-ocrv5`.
- Supports AutoJs6 calls such as `ocr.paddle.recognizeText(...)` and `ocr.paddle(...)`.
- Supports screenshot, local image path, and raw image data input, returning recognized text, confidence, rectangular bounds, and quadrilateral coordinates.
- Provides `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu`, and `tamil` product variants.
- Plugin metadata, usage instructions, README, and CHANGELOG are localized for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese.

******

### Model Profiles

******

Current Gradle flavors and model profiles include:

- `mobile`: default profile using `PP-OCRv5_mobile_det` and `PP-OCRv5_mobile_rec`.
- `server`: high-accuracy profile using `PP-OCRv5_server_det` and `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: reuse `PP-OCRv5_mobile_det` and use the matching PP-OCRv5 multilingual recognition model.

******

### Usage

******

Recognize text content in a screenshot:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

Recognize text content in a local image file:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

For more usage examples, refer to the [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) section in the AutoJs6 documentation.

******

### Prepare Models

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- The script downloads official ONNX tar packages from the Paddle model source and copies `inference.onnx` plus `inference.yml` into each flavor assets directory.
- Use options such as `--profile mobile` to prepare only one model profile.
- The mobile detection model is shared through `app/src/sharedMobileDet/assets`.

******

### Release History

******

# v1.0.0

###### 2026/07/17

* `Feature` Added the Paddle OCR PP-OCRv5 plugin service with default plugin ID `paddle-ocr-pp-ocrv5` and engine `paddle-ocr`
* `Feature` Added AutoJs6 OCR calls through `ocr.paddle.recognizeText(...)` and `ocr.paddle(...)`
* `Feature` Implemented PP-OCRv5 text detection, text recognition, CTC decoding, and quadrilateral result coordinates with ONNX Runtime Android and OpenCV
* `Feature` Added screenshot, local image path, and raw image data input, returning text, confidence, rectangular bounds, and timing metadata
* `Feature` Added product variants for `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu`, and `tamil`
* `Feature` Added localized plugin metadata and usage instructions for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese
* `Feature` Added JSON source based README and CHANGELOG generation through `.python/generate_markdown.py`

##### For more release history

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-en.md)

******

### Build

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

Build all profiles:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release build:

```powershell
.\gradlew.bat :app:assembleRelease
```

Build parameters come from `version.properties`; the current minimum SDK is 24 and target SDK is 36.

******

### Resource Layout

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contains localized plugin descriptions; `plugin_instruction.md` contains usage instructions displayed by the host. README and CHANGELOG files are generated from JSON sources by `.python/generate_markdown.py`, and the repository root keeps only `README.md`.

******

### Links

******

- AutoJs6 OCR documentation: https://docs.autojs6.com/#/ocr
- PaddleOCR official project: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX model source: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android documentation: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
