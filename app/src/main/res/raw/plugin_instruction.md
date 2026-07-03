Use Paddle OCR to recognize text content in a screenshot:

```js
/* Full form. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* Short form. */

ocr.paddle.recognizeText();

/* Minimal form. */

ocr.paddle();
```

Use Paddle OCR to recognize text content in a local image file (using `test.png` as an example):

```js
/* Full form. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* Short form. */

ocr.paddle.recognizeText("test.png");

/* Minimal form. */

ocr.paddle("test.png");
```

For more usage examples, refer to the [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr)
section in the AutoJs6 documentation.