******

### 发行历史

******

# v1.0.0

###### 2026/07/17

* `新增` Paddle OCR PP-OCRv5 插件服务, 默认插件 ID 为 `paddle-ocr-pp-ocrv5`, 引擎为 `paddle-ocr`
* `新增` 支持通过 AutoJs6 的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 调用 OCR 能力
* `新增` 基于 ONNX Runtime Android 和 OpenCV 实现 PP-OCRv5 文本检测/文本识别/CTC 解码和四点坐标结果
* `新增` 支持截图/本地图像路径和原始图像数据输入, 并返回文本/置信度/矩形边界和耗时信息
* `新增` 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 产品变体
* `新增` 插件信息和使用说明的多语言资源: 西班牙语/法语/俄语/阿拉伯语/日语/韩语/英语/简体中文/香港繁体/台湾繁体
* `新增` 基于 JSON 源文件和 `.python/generate_markdown.py` 生成多语言 README 与 CHANGELOG
