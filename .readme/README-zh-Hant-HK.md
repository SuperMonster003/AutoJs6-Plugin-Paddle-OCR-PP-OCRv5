<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>基於 PP-OCRv5 的 Paddle OCR 文本識別插件</p>

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

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 當前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### 簡介

******

AutoJs6 Paddle OCR PP-OCRv5 插件為 AutoJs6 提供基於 PaddleOCR ONNX Runtime 的文字檢測和文本識別能力, 支援移動端默認模型/高精度服務端模型和多語種識別模型.

******

### 功能

******

- 提供 `paddle-ocr` 插件服務, 默認插件 ID 為 `paddle-ocr-pp-ocrv5`.
- 支援 AutoJs6 中的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 調用.
- 支援屏幕截圖/本地圖像路徑和原始圖像數據輸入, 返回識別文本/置信度/矩形邊界和四點坐標.
- 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 產品變體.
- 插件資訊/使用說明/README 與 CHANGELOG 均支援西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體.

******

### 模型配置

******

目前 Gradle flavor 與模型配置包括:

- `mobile`: 默認配置, 使用 `PP-OCRv5_mobile_det` 和 `PP-OCRv5_mobile_rec`.
- `server`: 高精度配置, 使用 `PP-OCRv5_server_det` 和 `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: 複用 `PP-OCRv5_mobile_det`, 並使用對應 PP-OCRv5 多語種識別模型.

******

### 使用示例

******

識別屏幕截圖中的文本內容:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

識別本地圖像文件中的文本內容:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

更多使用方式, 可參閲 AutoJs6 應用文檔的 [光學字符識別 (OCR)](https://docs.autojs6.com/#/ocr) 章節.

******

### 準備模型

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- 腳本會從 Paddle 模型源下載官方 ONNX tar 包, 並複製 `inference.onnx` 與 `inference.yml` 到各 flavor assets.
- 可使用 `--profile mobile` 等參數只準備單個模型配置.
- 移動端檢測模型會共享到 `app/src/sharedMobileDet/assets`.

******

### 發行歷史

******

# v1.0.0

###### 2026/07/17

* `新增` Paddle OCR PP-OCRv5 插件服務, 默認插件 ID 為 `paddle-ocr-pp-ocrv5`, 引擎為 `paddle-ocr`
* `新增` 支援通過 AutoJs6 的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 調用 OCR 能力
* `新增` 基於 ONNX Runtime Android 和 OpenCV 實現 PP-OCRv5 文本檢測/文本識別/CTC 解碼和四點坐標結果
* `新增` 支援屏幕截圖/本地圖像路徑和原始圖像數據輸入, 並返回文本/置信度/矩形邊界和耗時資訊
* `新增` 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 產品變體
* `新增` 插件資訊和使用說明的多語言資源: 西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體
* `新增` 基於 JSON 源文件和 `.python/generate_markdown.py` 生成多語言 README 與 CHANGELOG

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

構建全部配置:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 構建:

```powershell
.\gradlew.bat :app:assembleRelease
```

構建參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36.

******

### 資源結構

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件描述本地化; `plugin_instruction.md` 提供宿主側展示的插件使用說明. README 與 CHANGELOG 由 `.python/generate_markdown.py` 根據 JSON 源文件生成, 根目錄僅保留 `README.md`.

******

### 相關鏈接

******

- AutoJs6 OCR 文檔: https://docs.autojs6.com/#/ocr
- PaddleOCR 官方項目: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX 模型源: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android 文檔: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
