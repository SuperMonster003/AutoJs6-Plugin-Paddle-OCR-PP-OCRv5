Используйте Paddle OCR для распознавания текстового содержимого на снимке экрана:

```js
/* Полная форма. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* Краткая форма. */

ocr.paddle.recognizeText();

/* Минимальная форма. */

ocr.paddle();
```

Используйте Paddle OCR для распознавания текстового содержимого в локальном файле изображения (на примере `test.png`):

```js
/* Полная форма. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* Краткая форма. */

ocr.paddle.recognizeText("test.png");

/* Минимальная форма. */

ocr.paddle("test.png");
```

Другие способы использования см. в разделе [Оптическое распознавание символов (OCR)](https://docs.autojs6.com/#/ocr)
документации AutoJs6.