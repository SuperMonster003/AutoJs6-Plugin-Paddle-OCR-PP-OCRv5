******

### Historial De Versiones

******

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
