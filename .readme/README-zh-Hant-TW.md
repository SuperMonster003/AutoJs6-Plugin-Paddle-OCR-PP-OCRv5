<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>以 PP-OCRv5 為基礎的 Paddle OCR 文字辨識外掛</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
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
- 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB
- 編碼影像最大為 64 MiB, 支援檔案描述元和管線傳輸

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

# v1.0.4

###### 2026/09/15

* `優化` 將 compileSdk 與 targetSdk 提升到 37 (Android 17), 外掛程式行為不受新目標版本影響

# v1.0.3

###### 2026/09/13

* `修復` 外掛中心顯示的版本與 ABI 資訊符合實際安裝的 APK
* `修復` 編碼影像最大為 64 MiB, 支援檔案描述元和管線傳輸
* `修復` 版本日期保持統一的英文格式
* `優化` 發行下載檔案產生前驗證 APK 版本, 簽章與完整變體集合
* `優化` 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB
* `優化` 擴充原生 ABI 封裝與外掛中繼資料至 arm64-v8a, armeabi-v7a, x86 和 x86_64, 同步通用 APK 與各 ABI 獨立 APK

# v1.0.2

###### 2026/09/12

* `修復` 修復 16 KB 分頁大小裝置上引擎初始化即當機 (SIGSEGV) 的問題: 隨附的 `libc++_shared.so` 更新為 NDK r28.2 建置版本, 其 RELRO 區段末尾不再與可寫資料共用記憶體頁 (arm64-v8a, armeabi-v7a)
* `修復` 修復載入體積較大的辨識模型時因 `OutOfMemoryError` 而靜默回傳空結果的問題: 模型資源現在只會複製到應用私有目錄一次, 並由 ONNX Runtime 以記憶體映射方式建立工作階段, 不再整體讀入 Java 堆積
* `修復` 部分 Gradle/AGP 組合下 Kotlin 原始碼未參與編譯, 以及重複定義 `WakeActivity` 導致的建置失敗
* `優化` 同步 OpenCV 4.8.0 原生程式庫至 NDK r28c (Clang 19.0.1) 重新建置版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-TW.md)

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

建置參數來自 `version.properties`, 目前最低 SDK 為 26, 目標 SDK 為 37.

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/docs/16kb.md)
