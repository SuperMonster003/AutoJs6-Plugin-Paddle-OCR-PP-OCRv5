******

### Release History

******

# v1.0.2

###### 2026/09/12

* `Fix` Fixed the engine crashing at initialization (SIGSEGV) on 16 KB page size devices: the bundled `libc++_shared.so` is now the NDK r28.2 build, whose RELRO segment no longer shares a page with writable data (arm64-v8a, armeabi-v7a)
* `Fix` Fixed large recognition models silently returning empty results after an `OutOfMemoryError`: model assets are now materialized once into app-private storage and memory-mapped by ONNX Runtime instead of being read into the Java heap
* `Improvement` Synced the OpenCV 4.8.0 native library to the NDK r28c (Clang 19.0.1) rebuild (donor: AutoJs6-Plugin-OpenCV); `libopencv_java4.so` for all 4 ABIs keeps 16 KB `PT_LOAD` alignment and ships with a provenance manifest

# v1.0.1

###### 2026/09/11

* `Improvement` Build verification of 16 KB page alignment for 64-bit native libraries, including manifest contract checks and JSON reports

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
