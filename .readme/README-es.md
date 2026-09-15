<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Complemento Paddle OCR de reconocimiento de texto basado en PP-OCRv5</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
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
- Las imágenes admiten hasta 16777216 píxeles; los búferes de imagen sin procesar se limitan a 64 MiB
- La imagen codificada admite hasta 64 MiB mediante descriptores de archivo y tuberías

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

# v1.0.3

###### 2026/09/13

* `Correccion` La versión y las ABI del centro de complementos coinciden con el APK instalado
* `Correccion` La imagen codificada admite hasta 64 MiB mediante descriptores de archivo y tuberías
* `Correccion` Las fechas de versión mantienen un formato uniforme en inglés
* `Mejora` Validación de las versiones, firmas y variantes completas de los APK antes de crear los archivos de descarga
* `Mejora` Las imágenes admiten hasta 16777216 píxeles; los búferes de imagen sin procesar se limitan a 64 MiB
* `Mejora` Ampliar el empaquetado de ABI nativas y los metadatos del complemento a arm64-v8a, armeabi-v7a, x86 y x86_64, con APK universales e individuales coherentes

# v1.0.2

###### 2026/09/12

* `Correccion` Corregido el bloqueo del motor al inicializarse (SIGSEGV) en dispositivos con páginas de 16 KB: la `libc++_shared.so` incluida es ahora la compilación del NDK r28.2, cuyo segmento RELRO ya no comparte página con datos escribibles (arm64-v8a, armeabi-v7a)
* `Correccion` Corregido que los modelos de reconocimiento grandes devolvieran resultados vacíos en silencio tras un `OutOfMemoryError`: los modelos se copian una sola vez al almacenamiento privado de la app y ONNX Runtime los mapea en memoria en lugar de leerlos en el heap de Java
* `Correccion` Fallos de compilación con algunas combinaciones de Gradle/AGP por fuentes Kotlin no compiladas o definiciones duplicadas de `WakeActivity`
* `Mejora` Sincronizada la biblioteca nativa OpenCV 4.8.0 con la reconstrucción NDK r28c (Clang 19.0.1) (donante: AutoJs6-Plugin-OpenCV); `libopencv_java4.so` de las 4 ABI mantiene la alineación `PT_LOAD` de 16 KB e incluye un manifiesto de provenance

# v1.0.1

###### 2026/09/11

* `Mejora` Verificación de compilación de la alineación de páginas de 16 KB en bibliotecas nativas de 64 bits, con controles del contrato manifest e informes JSON

##### Para mas historial de versiones

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/assets/doc/CHANGELOG-es.md)

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

Los parametros de compilacion provienen de `version.properties`; el SDK minimo actual es 26 y el SDK objetivo es 36.

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/docs/16kb.md)
