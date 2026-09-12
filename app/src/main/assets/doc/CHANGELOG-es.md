******

### Historial De Versiones

******

# v1.0.2

###### 2026/09/12

* `Correccion` Corregido el bloqueo del motor al inicializarse (SIGSEGV) en dispositivos con páginas de 16 KB: la `libc++_shared.so` incluida es ahora la compilación del NDK r28.2, cuyo segmento RELRO ya no comparte página con datos escribibles (arm64-v8a, armeabi-v7a)
* `Correccion` Corregido que los modelos de reconocimiento grandes devolvieran resultados vacíos en silencio tras un `OutOfMemoryError`: los modelos se copian una sola vez al almacenamiento privado de la app y ONNX Runtime los mapea en memoria en lugar de leerlos en el heap de Java
* `Correccion` Fallos de compilación con algunas combinaciones de Gradle/AGP por fuentes Kotlin no compiladas o definiciones duplicadas de `WakeActivity`
* `Mejora` Sincronizada la biblioteca nativa OpenCV 4.8.0 con la reconstrucción NDK r28c (Clang 19.0.1) (donante: AutoJs6-Plugin-OpenCV); `libopencv_java4.so` de las 4 ABI mantiene la alineación `PT_LOAD` de 16 KB e incluye un manifiesto de provenance

# v1.0.1

###### 2026/09/11

* `Mejora` Verificación de compilación de la alineación de páginas de 16 KB en bibliotecas nativas de 64 bits, con controles del contrato manifest e informes JSON

# v1.0.0

###### 2026/09/01

* `Funcion` Se agrego el servicio de complemento Paddle OCR PP-OCRv5 con el ID de complemento predeterminado `paddle-ocr-pp-ocrv5` y el motor `paddle-ocr`
* `Funcion` Se agregaron llamadas OCR de AutoJs6 mediante `ocr.paddle.recognizeText(...)` y `ocr.paddle(...)`
* `Funcion` Se implementaron deteccion de texto PP-OCRv5, reconocimiento de texto, decodificacion CTC y coordenadas cuadrilateras de resultado con ONNX Runtime Android y OpenCV
* `Funcion` Se agrego entrada por captura de pantalla, ruta de imagen local y datos de imagen sin procesar, con retorno de texto, confianza, limites rectangulares y metadatos de tiempo
* `Funcion` Se agregaron variantes de producto para `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` y `tamil`
* `Funcion` Se agregaron metadatos de complemento e instrucciones de uso localizados en espanol, frances, ruso, arabe, japones, coreano, ingles, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwan
* `Funcion` Se agrego generacion de README y CHANGELOG desde fuentes JSON mediante `.python/generate_markdown.py`
* `Correccion` El complemento no se podía activar desde el centro de complementos después de instalarlo en algunos sistemas
* `Mejora` Unificar el diseño del README y la gestión de versiones de la plataforma Gradle
