<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>以 PP-OCRv5 為基礎的 Paddle OCR 文字辨識外掛</p>

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
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- 繁體中文 (台灣) [zh-Hant-TW] # 目前
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

AutoJs6 Paddle OCR PP-OCRv5 外掛為 AutoJs6 提供以 PaddleOCR ONNX Runtime 為基礎的文字偵測和文字辨識能力, 支援行動端預設模型/高精度服務端模型和多語言辨識模型.

******

### 功能

******

- 提供 `paddle-ocr` 外掛服務, 預設外掛 ID 為 `paddle-ocr-pp-ocrv5`.
- 支援 AutoJs6 中的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 呼叫.
- 支援螢幕截圖/本地影像路徑和原始影像資料輸入, 回傳辨識文字/信賴度/矩形邊界和四點座標.
- 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 產品變體.
- 外掛資訊/使用說明/README 與 CHANGELOG 均支援西班牙文/法文/俄文/阿拉伯文/日文/韓文/英文/簡體中文/香港繁體/台灣繁體.

******

### 模型設定

******

目前 Gradle flavor 與模型設定包括:

- `mobile`: 預設設定, 使用 `PP-OCRv5_mobile_det` 和 `PP-OCRv5_mobile_rec`.
- `server`: 高精度設定, 使用 `PP-OCRv5_server_det` 和 `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: 共用 `PP-OCRv5_mobile_det`, 並使用對應 PP-OCRv5 多語言辨識模型.

******

### 使用範例

******

辨識螢幕截圖中的文字內容:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

辨識本地影像檔案中的文字內容:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

更多使用方式, 可參閱 AutoJs6 應用文件的 [光學字元識別 (OCR)](https://docs.autojs6.com/#/ocr) 章節.

******

### 準備模型

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- 腳本會從 Paddle 模型源下載官方 ONNX tar 包, 並複製 `inference.onnx` 與 `inference.yml` 到各 flavor assets.
- 可使用 `--profile mobile` 等參數只準備單一模型設定.
- 行動端偵測模型會共享到 `app/src/sharedMobileDet/assets`.

******

### 發行歷史

******

# v1.0.0

###### 2026/07/17

* `新增` Paddle OCR PP-OCRv5 外掛服務, 預設外掛 ID 為 `paddle-ocr-pp-ocrv5`, 引擎為 `paddle-ocr`
* `新增` 支援透過 AutoJs6 的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 呼叫 OCR 能力
* `新增` 以 ONNX Runtime Android 和 OpenCV 實作 PP-OCRv5 文字偵測/文字辨識/CTC 解碼和四點座標結果
* `新增` 支援螢幕截圖/本地影像路徑和原始影像資料輸入, 並回傳文字/信賴度/矩形邊界和耗時資訊
* `新增` 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 產品變體
* `新增` 外掛資訊和使用說明的多語言資源: 西班牙文/法文/俄文/阿拉伯文/日文/韓文/英文/簡體中文/香港繁體/台灣繁體
* `新增` 以 JSON 來源檔和 `.python/generate_markdown.py` 產生多語言 README 與 CHANGELOG

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-zh-Hant-TW.md)

******

### 建置

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

建置全部設定:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 建置:

```powershell
.\gradlew.bat :app:assembleRelease
```

建置參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36.

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

`strings.xml` 提供外掛描述在地化; `plugin_instruction.md` 提供宿主端展示的外掛使用說明. README 與 CHANGELOG 由 `.python/generate_markdown.py` 根據 JSON 來源檔產生, 根目錄僅保留 `README.md`.

******

### 相關連結

******

- AutoJs6 OCR 文件: https://docs.autojs6.com/#/ocr
- PaddleOCR 官方專案: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX 模型源: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android 文件: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
