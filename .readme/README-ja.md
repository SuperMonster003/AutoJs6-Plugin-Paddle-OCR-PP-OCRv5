<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>PP-OCRv5 ベースの Paddle OCR テキスト認識プラグイン</p>

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

### 言語

******

現在の README.md は次の言語に対応しています:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- 日本語 [ja] # 現在
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### 概要

******

AutoJs6 Paddle OCR PP-OCRv5 プラグインは, PaddleOCR ONNX Runtime を基盤としたテキスト検出とテキスト認識を AutoJs6 に提供します. モバイル既定モデル, 高精度サーバーモデル, 多言語認識モデルに対応します.

******

### 機能

******

- `paddle-ocr` プラグインサービスを提供し, 既定のプラグイン ID は `paddle-ocr-pp-ocrv5` です.
- AutoJs6 の `ocr.paddle.recognizeText(...)` と `ocr.paddle(...)` 呼び出しに対応します.
- スクリーンショット, ローカル画像パス, raw 画像データ入力に対応し, 認識テキスト, 信頼度, 矩形境界, 四点座標を返します.
- `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` の製品バリアントを提供します.
- プラグインメタデータ, 使用説明, README, CHANGELOG はスペイン語/フランス語/ロシア語/アラビア語/日本語/韓国語/英語/簡体字中国語/香港繁体字/台湾繁体字にローカライズされています.

******

### モデルプロファイル

******

現在の Gradle flavor とモデルプロファイルは次のとおりです:

- `mobile`: `PP-OCRv5_mobile_det` と `PP-OCRv5_mobile_rec` を使用する既定プロファイルです.
- `server`: `PP-OCRv5_server_det` と `PP-OCRv5_server_rec` を使用する高精度プロファイルです.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: `PP-OCRv5_mobile_det` を再利用し, 対応する PP-OCRv5 多言語認識モデルを使用します.

******

### 使用例

******

スクリーンショット内のテキスト内容を認識します:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

ローカル画像ファイル内のテキスト内容を認識します:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

その他の使用例は AutoJs6 ドキュメントの [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) セクションを参照してください.

******

### モデル準備

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- スクリプトは Paddle モデルソースから公式 ONNX tar パッケージをダウンロードし, `inference.onnx` と `inference.yml` を各 flavor assets にコピーします.
- `--profile mobile` などのオプションで単一モデルプロファイルのみを準備できます.
- モバイル検出モデルは `app/src/sharedMobileDet/assets` で共有されます.

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

##### 詳細なリリース履歴

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-ja.md)

******

### ビルド

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

全プロファイルをビルド:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release ビルド:

```powershell
.\gradlew.bat :app:assembleRelease
```

ビルドパラメータは `version.properties` から取得されます. 現在の最小 SDK は 24, ターゲット SDK は 36 です.

******

### リソース構成

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` にはローカライズされたプラグイン説明が含まれます. `plugin_instruction.md` にはホスト側で表示される使用説明が含まれます. README と CHANGELOG は `.python/generate_markdown.py` により JSON ソースから生成され, リポジトリルートには `README.md` のみを保持します.

******

### リンク

******

- AutoJs6 OCR ドキュメント: https://docs.autojs6.com/#/ocr
- PaddleOCR 公式プロジェクト: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX モデルソース: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android ドキュメント: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
