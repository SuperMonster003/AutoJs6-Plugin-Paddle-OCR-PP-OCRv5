******

### Historique Des Versions

******

# v1.0.0

###### 2026/07/17

* `Fonctionnalite` Ajout du service de plugin Paddle OCR PP-OCRv5 avec l'ID de plugin par defaut `paddle-ocr-pp-ocrv5` et le moteur `paddle-ocr`
* `Fonctionnalite` Ajout des appels OCR AutoJs6 via `ocr.paddle.recognizeText(...)` et `ocr.paddle(...)`
* `Fonctionnalite` Implementation de la detection de texte PP-OCRv5, de la reconnaissance de texte, du decodage CTC et des coordonnees quadrilaterales avec ONNX Runtime Android et OpenCV
* `Fonctionnalite` Ajout de l'entree par capture d'ecran, chemin d'image local et donnees d'image brutes, avec retour du texte, de la confiance, des limites rectangulaires et des metadonnees de duree
* `Fonctionnalite` Ajout des variantes de produit `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` et `tamil`
* `Fonctionnalite` Ajout des metadonnees de plugin et instructions d'utilisation localisees en espagnol, francais, russe, arabe, japonais, coreen, anglais, chinois simplifie, chinois traditionnel de Hong Kong et chinois traditionnel de Taiwan
* `Fonctionnalite` Ajout de la generation README et CHANGELOG depuis des sources JSON via `.python/generate_markdown.py`
