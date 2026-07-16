# v1.0.0

###### 2026/07/17

* `기능` 기본 플러그인 ID `paddle-ocr-pp-ocrv5` 및 엔진 `paddle-ocr`를 사용하는 Paddle OCR PP-OCRv5 플러그인 서비스를 추가
* `기능` `ocr.paddle.recognizeText(...)` 및 `ocr.paddle(...)`를 통한 AutoJs6 OCR 호출 추가
* `기능` ONNX Runtime Android 및 OpenCV로 PP-OCRv5 텍스트 검출, 텍스트 인식, CTC 디코딩, 사각형 결과 좌표 구현
* `기능` 스크린샷, 로컬 이미지 경로, raw 이미지 데이터 입력을 추가하고 텍스트, 신뢰도, 사각형 경계, 시간 메타데이터 반환
* `기능` `mobile`/`server`/`english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil` 제품 변형 추가
* `기능` 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/간체 중국어/홍콩 번체/대만 번체용 플러그인 메타데이터 및 사용 설명 추가
* `기능` `.python/generate_markdown.py`를 통한 JSON 소스 기반 README 및 CHANGELOG 생성 추가
