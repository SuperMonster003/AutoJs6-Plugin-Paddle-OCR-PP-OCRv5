******

### 發行歷史

******

# v1.0.3

###### 2026/09/13

* `修復` 外掛中心顯示的版本與 ABI 資訊符合實際安裝的 APK
* `修復` 編碼圖像最大為 64 MiB, 支援檔案描述符和管道傳輸
* `修復` 版本日期保持統一的英文格式
* `優化` 發佈下載檔案產生前校驗 APK 版本, 簽署與完整變體集合
* `優化` 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB

# v1.0.2

###### 2026/09/12

* `修復` 修復 16 KB 頁大小設備上引擎初始化即崩潰 (SIGSEGV) 的問題: 隨包的 `libc++_shared.so` 更新為 NDK r28.2 構建版本, 其 RELRO 段末尾不再與可寫資料共用記憶體頁 (arm64-v8a, armeabi-v7a)
* `修復` 修復載入體積較大的識別模型時因 `OutOfMemoryError` 而靜默返回空結果的問題: 模型資源現在只會複製到應用私有目錄一次, 並由 ONNX Runtime 以記憶體映射方式建立會話, 不再整體讀入 Java 堆
* `修復` 部分 Gradle/AGP 組合下 Kotlin 原始碼未參與編譯, 以及重複定義 `WakeActivity` 導致的建置失敗
* `優化` 同步 OpenCV 4.8.0 原生庫至 NDK r28c (Clang 19.0.1) 重編版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

# v1.0.1

###### 2026/09/11

* `優化` 建置階段校驗 64 位原生程式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告

# v1.0.0

###### 2026/09/01

* `新增` Paddle OCR PP-OCRv5 插件服務, 默認插件 ID 為 `paddle-ocr-pp-ocrv5`, 引擎為 `paddle-ocr`
* `新增` 支援通過 AutoJs6 的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 調用 OCR 能力
* `新增` 基於 ONNX Runtime Android 和 OpenCV 實現 PP-OCRv5 文本檢測/文本識別/CTC 解碼和四點坐標結果
* `新增` 支援屏幕截圖/本地圖像路徑和原始圖像數據輸入, 並返回文本/置信度/矩形邊界和耗時資訊
* `新增` 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 產品變體
* `新增` 插件資訊和使用說明的多語言資源: 西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體
* `新增` 基於 JSON 源文件和 `.python/generate_markdown.py` 生成多語言 README 與 CHANGELOG
* `修復` 部分系統安裝後無法透過插件中心激活的問題
* `優化` 統一 README 版式與 Gradle 平台版本管理方式
