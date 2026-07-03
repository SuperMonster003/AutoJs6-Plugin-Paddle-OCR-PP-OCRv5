استخدم Paddle OCR للتعرف على المحتوى النصي في لقطة الشاشة:

```js
/* الصيغة الكاملة. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* الصيغة المختصرة. */

ocr.paddle.recognizeText();

/* الصيغة الابسط. */

ocr.paddle();
```

استخدم Paddle OCR للتعرف على المحتوى النصي في ملف صورة محلي (باستخدام `test.png` كمثال):

```js
/* الصيغة الكاملة. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* الصيغة المختصرة. */

ocr.paddle.recognizeText("test.png");

/* الصيغة الابسط. */

ocr.paddle("test.png");
```

لمزيد من طرق الاستخدام، راجع قسم [التعرف الضوئي على الحروف (OCR)](https://docs.autojs6.com/#/ocr)
في وثائق AutoJs6.