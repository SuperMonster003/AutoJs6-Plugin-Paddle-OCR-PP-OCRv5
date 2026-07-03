Paddle OCR を使って、スクリーンショット内のテキスト内容を認識します:

```js
/* 完全な書き方. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* 簡略形. */

ocr.paddle.recognizeText();

/* 最小形. */

ocr.paddle();
```

Paddle OCR を使って、ローカル画像ファイル内のテキスト内容を認識します (`test.png` を例とします):

```js
/* 完全な書き方. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* 簡略形. */

ocr.paddle.recognizeText("test.png");

/* 最小形. */

ocr.paddle("test.png");
```

その他の使用方法については、AutoJs6 アプリドキュメントの [光学文字認識 (OCR)](https://docs.autojs6.com/#/ocr)
セクションを参照してください.