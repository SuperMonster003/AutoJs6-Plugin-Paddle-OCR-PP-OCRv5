******

### リリース履歴

******

# v1.0.0

###### 2026/07/17

* `機能` 既定のプラグイン ID `paddle-ocr-pp-ocrv5` とエンジン `paddle-ocr` を持つ Paddle OCR PP-OCRv5 プラグインサービスを追加
* `機能` `ocr.paddle.recognizeText(...)` と `ocr.paddle(...)` による AutoJs6 OCR 呼び出しを追加
* `機能` ONNX Runtime Android と OpenCV で PP-OCRv5 テキスト検出, テキスト認識, CTC デコード, 四点座標結果を実装
* `機能` スクリーンショット, ローカル画像パス, raw 画像データ入力を追加し, テキスト, 信頼度, 矩形境界, 時間メタデータを返すように変更
* `機能` `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` の製品バリアントを追加
* `機能` スペイン語/フランス語/ロシア語/アラビア語/日本語/韓国語/英語/簡体字中国語/香港繁体字/台湾繁体字のプラグインメタデータと使用説明を追加
* `機能` `.python/generate_markdown.py` による JSON ソースベースの README と CHANGELOG 生成を追加
