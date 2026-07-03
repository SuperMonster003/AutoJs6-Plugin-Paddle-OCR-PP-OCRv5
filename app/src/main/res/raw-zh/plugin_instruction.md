使用 Paddle OCR 识别屏幕截图中的文本内容:

```js
/* 完整写法. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* 简写. */

ocr.paddle.recognizeText();

/* 极简. */

ocr.paddle();
```

使用 Paddle OCR 识别本地图像文件中的文本内容 (以 `test.png` 为例):

```js
/* 完整写法. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* 简写. */

ocr.paddle.recognizeText("test.png");

/* 极简. */

ocr.paddle("test.png");
```

更多使用方式, 可参阅 AutoJs6 应用文档的 [光学字符识别 (OCR)](https://docs.autojs6.com/#/ocr) 章节.