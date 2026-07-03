Paddle OCR을 사용해 화면 캡처의 텍스트 내용을 인식합니다:

```js
/* 전체 형식. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* 축약 형식. */

ocr.paddle.recognizeText();

/* 최소 형식. */

ocr.paddle();
```

Paddle OCR을 사용해 로컬 이미지 파일의 텍스트 내용을 인식합니다 (`test.png` 예시):

```js
/* 전체 형식. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* 축약 형식. */

ocr.paddle.recognizeText("test.png");

/* 최소 형식. */

ocr.paddle("test.png");
```

더 많은 사용 방법은 AutoJs6 앱 문서의 [광학 문자 인식 (OCR)](https://docs.autojs6.com/#/ocr)
섹션을 참고하세요.