Usa Paddle OCR para reconocer el contenido de texto en una captura de pantalla:

```js
/* Forma completa. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* Forma abreviada. */

ocr.paddle.recognizeText();

/* Forma minima. */

ocr.paddle();
```

Usa Paddle OCR para reconocer el contenido de texto en un archivo de imagen local (tomando `test.png` como ejemplo):

```js
/* Forma completa. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* Forma abreviada. */

ocr.paddle.recognizeText("test.png");

/* Forma minima. */

ocr.paddle("test.png");
```

Para mas formas de uso, consulta la seccion [Reconocimiento optico de caracteres (OCR)](https://docs.autojs6.com/#/ocr)
de la documentacion de AutoJs6.