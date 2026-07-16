<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>PP-OCRv5 기반 Paddle OCR 텍스트 인식 플러그인</p>

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

### 언어

******

현재 README.md는 다음 언어를 지원합니다:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- 한국어 [ko] # 현재
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### 소개

******

AutoJs6 Paddle OCR PP-OCRv5 플러그인은 PaddleOCR ONNX Runtime 기반 텍스트 검출 및 텍스트 인식을 AutoJs6에 제공합니다. 모바일 기본 모델, 고정확도 서버 모델, 다국어 인식 모델을 지원합니다.

******

### 기능

******

- `paddle-ocr` 플러그인 서비스를 제공하며 기본 플러그인 ID는 `paddle-ocr-pp-ocrv5`입니다.
- AutoJs6의 `ocr.paddle.recognizeText(...)` 및 `ocr.paddle(...)` 호출을 지원합니다.
- 스크린샷, 로컬 이미지 경로, raw 이미지 데이터 입력을 지원하며 인식 텍스트, 신뢰도, 사각형 경계, 네 점 좌표를 반환합니다.
- `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 제품 변형을 제공합니다.
- 플러그인 메타데이터, 사용 설명, README, CHANGELOG는 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/간체 중국어/홍콩 번체/대만 번체로 현지화되어 있습니다.

******

### 모델 프로필

******

현재 Gradle flavor 및 모델 프로필은 다음과 같습니다:

- `mobile`: `PP-OCRv5_mobile_det` 및 `PP-OCRv5_mobile_rec`를 사용하는 기본 프로필입니다.
- `server`: `PP-OCRv5_server_det` 및 `PP-OCRv5_server_rec`를 사용하는 고정확도 프로필입니다.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: `PP-OCRv5_mobile_det`를 재사용하고 해당 PP-OCRv5 다국어 인식 모델을 사용합니다.

******

### 사용 예

******

스크린샷의 텍스트 내용을 인식합니다:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

로컬 이미지 파일의 텍스트 내용을 인식합니다:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

더 많은 사용 예는 AutoJs6 문서의 [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) 섹션을 참고하세요.

******

### 모델 준비

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- 스크립트는 Paddle 모델 소스에서 공식 ONNX tar 패키지를 다운로드하고 `inference.onnx` 및 `inference.yml`을 각 flavor assets에 복사합니다.
- `--profile mobile` 같은 옵션으로 하나의 모델 프로필만 준비할 수 있습니다.
- 모바일 검출 모델은 `app/src/sharedMobileDet/assets`를 통해 공유됩니다.

******

### 릴리스 기록

******

# v1.0.0

###### 2026/07/17

* `기능` 기본 플러그인 ID `paddle-ocr-pp-ocrv5` 및 엔진 `paddle-ocr`를 사용하는 Paddle OCR PP-OCRv5 플러그인 서비스를 추가
* `기능` `ocr.paddle.recognizeText(...)` 및 `ocr.paddle(...)`를 통한 AutoJs6 OCR 호출 추가
* `기능` ONNX Runtime Android 및 OpenCV로 PP-OCRv5 텍스트 검출, 텍스트 인식, CTC 디코딩, 사각형 결과 좌표 구현
* `기능` 스크린샷, 로컬 이미지 경로, raw 이미지 데이터 입력을 추가하고 텍스트, 신뢰도, 사각형 경계, 시간 메타데이터 반환
* `기능` `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 제품 변형 추가
* `기능` 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/간체 중국어/홍콩 번체/대만 번체용 플러그인 메타데이터 및 사용 설명 추가
* `기능` `.python/generate_markdown.py`를 통한 JSON 소스 기반 README 및 CHANGELOG 생성 추가

##### 더 많은 릴리스 기록

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-ko.md)

******

### 빌드

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

모든 프로필 빌드:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 빌드:

```powershell
.\gradlew.bat :app:assembleRelease
```

빌드 매개변수는 `version.properties`에서 가져옵니다. 현재 최소 SDK는 24이고 대상 SDK는 36입니다.

******

### 리소스 구조

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml`에는 현지화된 플러그인 설명이 포함됩니다. `plugin_instruction.md`에는 호스트에서 표시하는 사용 설명이 포함됩니다. README와 CHANGELOG는 `.python/generate_markdown.py`가 JSON 소스에서 생성하며, 저장소 루트에는 `README.md`만 유지합니다.

******

### 링크

******

- AutoJs6 OCR 문서: https://docs.autojs6.com/#/ocr
- PaddleOCR 공식 프로젝트: https://github.com/PaddlePaddle/PaddleOCR
- PP-OCRv5 ONNX 모델 소스: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- ONNX Runtime Android 문서: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
