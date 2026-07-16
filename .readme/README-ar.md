<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>ملحق Paddle OCR للتعرف على النصوص استنادا إلى PP-OCRv5</p>

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

### اللغات

******

يدعم README.md الحالي اللغات التالية:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- العربية [ar] # الحالي

******

### مقدمة

******

يوفر ملحق AutoJs6 Paddle OCR PP-OCRv5 لا AutoJs6 كشف النص والتعرف على النص استنادا إلى PaddleOCR ONNX Runtime. يدعم نموذج الهاتف الافتراضي, ونموذج الخادم عالي الدقة, ونماذج التعرف متعددة اللغات.

******

### الميزات

******

- يوفر خدمة الملحق `paddle-ocr` مع معرف الملحق الافتراضي `paddle-ocr-pp-ocrv5`.
- يدعم استدعاءات AutoJs6 مثل `ocr.paddle.recognizeText(...)` و `ocr.paddle(...)`.
- يدعم لقطة الشاشة, ومسار الصورة المحلي, وبيانات الصورة الخام, ويعيد النص المتعرف عليه, والثقة, والحدود المستطيلة, وإحداثيات الرباعي.
- يوفر متغيرات المنتج `mobile` و `server` و `english` و `korean` و `latin` و `eslav` و `thai` و `greek` و `arabic` و `cyrillic` و `devanagari` و `telugu` و `tamil`.
- تمت ترجمة بيانات الملحق وتعليمات الاستخدام و README و CHANGELOG إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية في هونغ كونغ والصينية التقليدية في تايوان.

******

### ملفات النموذج

******

تتضمن Gradle flavors وملفات النموذج الحالية:

- `mobile`: ملف افتراضي يستخدم `PP-OCRv5_mobile_det` و `PP-OCRv5_mobile_rec`.
- `server`: ملف عالي الدقة يستخدم `PP-OCRv5_server_det` و `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: يعيد استخدام `PP-OCRv5_mobile_det` ويستخدم نموذج التعرف متعدد اللغات PP-OCRv5 المطابق.

******

### الاستخدام

******

التعرف على محتوى النص في لقطة شاشة:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

التعرف على محتوى النص في ملف صورة محلي:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

لمزيد من أمثلة الاستخدام, راجع قسم [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) في وثائق AutoJs6.

******

### تحضير النماذج

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- يقوم السكربت بتنزيل حزم ONNX tar الرسمية من مصدر نماذج Paddle وينسخ `inference.onnx` مع `inference.yml` إلى assets لكل flavor.
- استخدم خيارات مثل `--profile mobile` لتحضير ملف نموذج واحد فقط.
- تتم مشاركة نموذج كشف الهاتف عبر `app/src/sharedMobileDet/assets`.

******

### سجل الإصدارات

******

# v1.0.0

###### 2026/07/17

* `ميزة` تمت إضافة خدمة ملحق Paddle OCR PP-OCRv5 مع معرف الملحق الافتراضي `paddle-ocr-pp-ocrv5` والمحرك `paddle-ocr`
* `ميزة` تمت إضافة استدعاءات OCR في AutoJs6 عبر `ocr.paddle.recognizeText(...)` و `ocr.paddle(...)`
* `ميزة` تم تنفيذ كشف النص PP-OCRv5 والتعرف على النص وفك ترميز CTC وإحداثيات النتيجة الرباعية باستخدام ONNX Runtime Android و OpenCV
* `ميزة` تمت إضافة إدخال لقطة الشاشة ومسار الصورة المحلي وبيانات الصورة الخام مع إرجاع النص والثقة والحدود المستطيلة وبيانات الوقت
* `ميزة` تمت إضافة متغيرات المنتج `mobile` و `server` و `english` و `korean` و `latin` و `eslav` و `thai` و `greek` و `arabic` و `cyrillic` و `devanagari` و `telugu` و `tamil`
* `ميزة` تمت إضافة بيانات الملحق وتعليمات الاستخدام المترجمة للإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية في هونغ كونغ والصينية التقليدية في تايوان
* `ميزة` تمت إضافة إنشاء README و CHANGELOG من مصادر JSON عبر `.python/generate_markdown.py`

##### لمزيد من سجل الإصدارات

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-ar.md)

******

### البناء

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

بناء كل الملفات:

```powershell
.\gradlew.bat :app:assembleDebug
```

بناء Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

تأتي معاملات البناء من `version.properties`; الحد الأدنى الحالي من SDK هو 24 و SDK الهدف هو 36.

******

### بنية الموارد

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

يحتوي `strings.xml` على أوصاف الملحق المترجمة; ويحتوي `plugin_instruction.md` على تعليمات الاستخدام التي يعرضها المضيف. يتم إنشاء README و CHANGELOG من مصادر JSON بواسطة `.python/generate_markdown.py`, ويحتفظ جذر المستودع بملف `README.md` فقط.

******

### روابط

******

- وثائق AutoJs6 OCR: https://docs.autojs6.com/#/ocr
- مشروع PaddleOCR الرسمي: https://github.com/PaddlePaddle/PaddleOCR
- مصدر نماذج PP-OCRv5 ONNX: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- وثائق ONNX Runtime Android: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
