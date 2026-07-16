<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>Плагин распознавания текста Paddle OCR на базе PP-OCRv5</p>

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

### Языки

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### Введение

******

Плагин AutoJs6 Paddle OCR PP-OCRv5 предоставляет AutoJs6 обнаружение текста и распознавание текста на базе PaddleOCR ONNX Runtime. Он поддерживает мобильную модель по умолчанию, высокоточную серверную модель и многоязычные модели распознавания.

******

### Функции

******

- Предоставляет сервис плагина `paddle-ocr` с ID плагина по умолчанию `paddle-ocr-pp-ocrv5`.
- Поддерживает вызовы AutoJs6, такие как `ocr.paddle.recognizeText(...)` и `ocr.paddle(...)`.
- Поддерживает снимки экрана, локальные пути к изображениям и входные raw-данные изображения, возвращая распознанный текст, уверенность, прямоугольные границы и координаты четырехугольника.
- Предоставляет варианты продукта `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` и `tamil`.
- Метаданные плагина, инструкции, README и CHANGELOG локализованы на испанский, французский, русский, арабский, японский, корейский, английский, упрощенный китайский, гонконгский традиционный китайский и тайваньский традиционный китайский.

******

### Профили Моделей

******

Текущие Gradle flavors и профили моделей включают:

- `mobile`: профиль по умолчанию с `PP-OCRv5_mobile_det` и `PP-OCRv5_mobile_rec`.
- `server`: высокоточный профиль с `PP-OCRv5_server_det` и `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: повторно использует `PP-OCRv5_mobile_det` и соответствующую многоязычную модель распознавания PP-OCRv5.

******

### Использование

******

Распознать текстовое содержимое на снимке экрана:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

Распознать текстовое содержимое в локальном файле изображения:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

Дополнительные примеры использования см. в разделе [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) документации AutoJs6.

******

### Подготовка Моделей

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- Скрипт загружает официальные ONNX tar-пакеты из источника моделей Paddle и копирует `inference.onnx` вместе с `inference.yml` в assets каждого flavor.
- Используйте параметры вроде `--profile mobile`, чтобы подготовить только один профиль модели.
- Мобильная модель обнаружения совместно используется через `app/src/sharedMobileDet/assets`.

******

### История Выпусков

******

# v1.0.0

###### 2026/07/17

* `Функция` Добавлен сервис плагина Paddle OCR PP-OCRv5 с ID плагина по умолчанию `paddle-ocr-pp-ocrv5` и движком `paddle-ocr`
* `Функция` Добавлены OCR-вызовы AutoJs6 через `ocr.paddle.recognizeText(...)` и `ocr.paddle(...)`
* `Функция` Реализованы обнаружение текста PP-OCRv5, распознавание текста, CTC-декодирование и координаты четырехугольника результата с ONNX Runtime Android и OpenCV
* `Функция` Добавлен ввод снимка экрана, локального пути к изображению и raw-данных изображения с возвратом текста, уверенности, прямоугольных границ и метаданных времени
* `Функция` Добавлены варианты продукта `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` и `tamil`
* `Функция` Добавлены локализованные метаданные плагина и инструкции для испанского, французского, русского, арабского, японского, корейского, английского, упрощенного китайского, гонконгского традиционного китайского и тайваньского традиционного китайского
* `Функция` Добавлена генерация README и CHANGELOG из JSON-источников через `.python/generate_markdown.py`

##### Больше истории выпусков

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-ru.md)

******

### Сборка

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

Собрать все профили:

```powershell
.\gradlew.bat :app:assembleDebug
```

Release сборка:

```powershell
.\gradlew.bat :app:assembleRelease
```

Параметры сборки берутся из `version.properties`; текущий минимальный SDK равен 24, целевой SDK равен 36.

******

### Структура Ресурсов

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` содержит локализованные описания плагина; `plugin_instruction.md` содержит инструкции, отображаемые хостом. README и CHANGELOG генерируются из JSON-источников с помощью `.python/generate_markdown.py`, а в корне репозитория остается только `README.md`.

******

### Ссылки

******

- Документация AutoJs6 OCR: https://docs.autojs6.com/#/ocr
- Официальный проект PaddleOCR: https://github.com/PaddlePaddle/PaddleOCR
- Источник ONNX моделей PP-OCRv5: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- Документация ONNX Runtime Android: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
