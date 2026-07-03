使用 Paddle OCR 識別螢幕截圖中的文字內容:

```js
/* 完整寫法. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* 簡寫. */

ocr.paddle.recognizeText();

/* 極簡. */

ocr.paddle();
```

使用 Paddle OCR 識別本地影像檔案中的文字內容 (以 `test.png` 為例):

```js
/* 完整寫法. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* 簡寫. */

ocr.paddle.recognizeText("test.png");

/* 極簡. */

ocr.paddle("test.png");
```

更多使用方式, 可參閱 AutoJs6 應用文件的 [光學字元識別 (OCR)](https://docs.autojs6.com/#/ocr) 章節.