<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>基于 PP-OCRv5 的 Paddle OCR 文本识别插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 语言 (Languages)

******

当前 README.md 支持以下语言:

- 简体中文 [zh-Hans] # 当前
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### 简介

******

AutoJs6 Paddle OCR PP-OCRv5 插件为 AutoJs6 提供基于 PaddleOCR ONNX Runtime 的文字检测和文本识别能力, 支持移动端默认模型/高精度服务端模型和多语种识别模型.

******

### 功能

******

- 提供 `paddle-ocr` 插件服务, 默认插件 ID 为 `paddle-ocr-pp-ocrv5`.
- 支持 AutoJs6 中的 `ocr.paddle.recognizeText(...)` 和 `ocr.paddle(...)` 调用.
- 支持截图/本地图像路径和原始图像数据输入, 返回识别文本/置信度/矩形边界和四点坐标.
- 提供 `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 产品变体.
- 插件信息/使用说明/README 与 CHANGELOG 均支持西班牙语/法语/俄语/阿拉伯语/日语/韩语/英语/简体中文/香港繁体/台湾繁体.
- 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB
- 编码图像最大为 64 MiB, 支持文件描述符和管道传输

******

### 模型配置

******

当前 Gradle flavor 与模型配置包括:

- `mobile`: 默认配置, 使用 `PP-OCRv5_mobile_det` 和 `PP-OCRv5_mobile_rec`.
- `server`: 高精度配置, 使用 `PP-OCRv5_server_det` 和 `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: 复用 `PP-OCRv5_mobile_det`, 并使用对应 PP-OCRv5 多语种识别模型.

******

### 使用示例

******

识别截图中的文本内容:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

识别本地图像文件中的文本内容:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

更多使用方式, 可参阅 AutoJs6 应用文档的 [光学字符识别 (OCR)](https://docs.autojs6.com/#/ocr) 章节.

******

### 准备模型

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- 脚本会从 Paddle 模型源下载官方 ONNX tar 包, 并复制 `inference.onnx` 与 `inference.yml` 到各 flavor assets.
- 可使用 `--profile mobile` 等参数只准备单个模型配置.
- 移动端检测模型会共享到 `app/src/sharedMobileDet/assets`.

******

### 发行历史

******

# v1.0.4

###### 2026/09/19

* `修复` AGP 9.1 构建时的 SDK XML v4 解析警告, 以及 JVM 单元测试误触发 APK 原生库对齐检查的问题 (共享构建插件 1.8.3)
* `优化` compileSdk/targetSdk 升级至 37 (Android 17)

# v1.0.3

###### 2026/09/13

* `修复` 插件中心显示的版本及 ABI 信息与实际安装包不一致的问题
* `修复` 编码图像的文件描述符及管道输入兼容性问题 (文件大小上限为 64 MiB)
* `修复` 版本日期受构建环境语言影响, 未统一使用英文格式的问题
* `优化` 发布前校验 APK 版本, 签名及变体完整性
* `优化` 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB
* `优化` 原生库支持 arm64-v8a/armeabi-v7a/x86/x86_64, 并提供通用及各 ABI 独立安装包

# v1.0.2

###### 2026/09/12

* `修复` 16 KB 内存页设备初始化引擎时出现 SIGSEGV 崩溃的问题, libc++_shared.so 改用 NDK r28.2 构建版本 (arm64-v8a/armeabi-v7a)
* `修复` 加载较大识别模型时因 OutOfMemoryError 而返回空结果的问题, 模型改为缓存至应用私有目录并通过内存映射加载
* `修复` 部分 Gradle/AGP 组合下 Kotlin 源码未参与编译, 以及 WakeActivity 重复定义导致构建失败的问题
* `优化` OpenCV 4.8.0 原生库改用 NDK r28c (Clang 19.0.1) 构建, 4 个 ABI 均支持 16 KB 内存页并附带构建来源清单 (来源: AutoJs6-Plugin-OpenCV)

##### 更多发行历史可参阅

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hans.md)

******

### 构建

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

构建全部配置:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 构建:

```powershell
.\gradlew.bat :app:assembleRelease
```

构建参数来自 `version.properties`, 当前最低 SDK 为 26, 目标 SDK 为 37.

******

### 资源结构

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件描述本地化; `plugin_instruction.md` 提供宿主侧展示的插件使用说明. README 与 CHANGELOG 由 `.python/generate_markdown.py` 根据 JSON 源文件生成, 根目录仅保留 `README.md`.

******

### 相关链接

******

- AutoJs6 OCR 文档: https://docs.autojs6.com/#/ocr
- PaddleOCR 官方项目: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX 模型源: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android 文档: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/docs/16kb.md)
