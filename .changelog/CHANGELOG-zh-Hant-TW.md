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
