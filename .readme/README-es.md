<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>Complemento Paddle OCR de reconocimiento de texto basado en PP-OCRv5</p>

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

### Idiomas

******

El README.md actual admite los siguientes idiomas:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-fr.md)
- Español [es] # actual
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### Introduccion

******

El complemento AutoJs6 Paddle OCR PP-OCRv5 proporciona a AutoJs6 deteccion de texto y reconocimiento de texto basados en PaddleOCR ONNX Runtime. Admite el modelo movil predeterminado, el modelo de servidor de alta precision y modelos de reconocimiento multilingues.

******

### Funciones

******

- Proporciona el servicio de complemento `paddle-ocr` con el ID de complemento predeterminado `paddle-ocr-pp-ocrv5`.
- Admite llamadas de AutoJs6 como `ocr.paddle.recognizeText(...)` y `ocr.paddle(...)`.
- Admite capturas de pantalla, rutas de imagen locales y datos de imagen sin procesar, y devuelve texto reconocido, confianza, limites rectangulares y coordenadas cuadrilateras.
- Proporciona variantes de producto `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` y `tamil`.
- Los metadatos del complemento, las instrucciones de uso, el README y el CHANGELOG estan localizados en espanol, frances, ruso, arabe, japones, coreano, ingles, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwan.

******

### Perfiles De Modelo

******

Los flavors de Gradle y perfiles de modelo actuales incluyen:

- `mobile`: perfil predeterminado que usa `PP-OCRv5_mobile_det` y `PP-OCRv5_mobile_rec`.
- `server`: perfil de alta precision que usa `PP-OCRv5_server_det` y `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: reutiliza `PP-OCRv5_mobile_det` y usa el modelo de reconocimiento multilingue PP-OCRv5 correspondiente.

******

### Uso

******

Reconocer el contenido de texto de una captura de pantalla:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

Reconocer el contenido de texto de un archivo de imagen local:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

Para mas ejemplos de uso, consulta la seccion [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) de la documentacion de AutoJs6.

******

### Preparar Modelos

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- El script descarga los paquetes tar ONNX oficiales desde la fuente de modelos Paddle y copia `inference.onnx` junto con `inference.yml` en los assets de cada flavor.
- Usa opciones como `--profile mobile` para preparar solo un perfil de modelo.
- El modelo de deteccion movil se comparte mediante `app/src/sharedMobileDet/assets`.

******

### Historial De Versiones

******

# v1.0.0

###### 2026/07/17

* `Funcion` Se agrego el servicio de complemento Paddle OCR PP-OCRv5 con el ID de complemento predeterminado `paddle-ocr-pp-ocrv5` y el motor `paddle-ocr`
* `Funcion` Se agregaron llamadas OCR de AutoJs6 mediante `ocr.paddle.recognizeText(...)` y `ocr.paddle(...)`
* `Funcion` Se implementaron deteccion de texto PP-OCRv5, reconocimiento de texto, decodificacion CTC y coordenadas cuadrilateras de resultado con ONNX Runtime Android y OpenCV
* `Funcion` Se agrego entrada por captura de pantalla, ruta de imagen local y datos de imagen sin procesar, con retorno de texto, confianza, limites rectangulares y metadatos de tiempo
* `Funcion` Se agregaron variantes de producto para `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` y `tamil`
* `Funcion` Se agregaron metadatos de complemento e instrucciones de uso localizados en espanol, frances, ruso, arabe, japones, coreano, ingles, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwan
* `Funcion` Se agrego generacion de README y CHANGELOG desde fuentes JSON mediante `.python/generate_markdown.py`

##### Para mas historial de versiones

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-es.md)

******

### Compilacion

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

Compilar todos los perfiles:

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilacion Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Los parametros de compilacion provienen de `version.properties`; el SDK minimo actual es 24 y el SDK objetivo es 36.

******

### Estructura De Recursos

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contiene descripciones localizadas del complemento; `plugin_instruction.md` contiene las instrucciones de uso que muestra el host. README y CHANGELOG se generan desde fuentes JSON mediante `.python/generate_markdown.py`, y la raiz del repositorio conserva solo `README.md`.

******

### Enlaces

******

- Documentacion OCR de AutoJs6: https://docs.autojs6.com/#/ocr
- Proyecto oficial PaddleOCR: https://github.com/PaddlePaddle/PaddleOCR
- Fuente de modelos ONNX PP-OCRv5: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- Documentacion ONNX Runtime Android: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
