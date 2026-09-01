******

### Release History

******

# v1.0.0

###### 2026/09/01

* `Feature` Added the Paddle OCR PP-OCRv5 plugin service with default plugin ID `paddle-ocr-pp-ocrv5` and engine `paddle-ocr`
* `Feature` Added AutoJs6 OCR calls through `ocr.paddle.recognizeText(...)` and `ocr.paddle(...)`
* `Feature` Implemented PP-OCRv5 text detection, text recognition, CTC decoding, and quadrilateral result coordinates with ONNX Runtime Android and OpenCV
* `Feature` Added screenshot, local image path, and raw image data input, returning text, confidence, rectangular bounds, and timing metadata
* `Feature` Added product variants for `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu`, and `tamil`
* `Feature` Added localized plugin metadata and usage instructions for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese
* `Feature` Added JSON source based README and CHANGELOG generation through `.python/generate_markdown.py`
* `Fix` The plugin could not be activated from Plugin Center after installation on some systems
* `Improvement` Standardize the README layout and Gradle platform version management
