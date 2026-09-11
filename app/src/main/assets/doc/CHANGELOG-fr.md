******

### Historique Des Versions

******

# v1.0.1

###### 2026/09/11

* `Amelioration` Vérification à la compilation de l'alignement des pages de 16 KB des bibliothèques natives 64 bits, avec contrôle du contrat manifest et rapports JSON

# v1.0.0

###### 2026/09/01

* `Fonctionnalite` Ajout du service de plugin Paddle OCR PP-OCRv5 avec l'ID de plugin par defaut `paddle-ocr-pp-ocrv5` et le moteur `paddle-ocr`
* `Fonctionnalite` Ajout des appels OCR AutoJs6 via `ocr.paddle.recognizeText(...)` et `ocr.paddle(...)`
* `Fonctionnalite` Implementation de la detection de texte PP-OCRv5, de la reconnaissance de texte, du decodage CTC et des coordonnees quadrilaterales avec ONNX Runtime Android et OpenCV
* `Fonctionnalite` Ajout de l'entree par capture d'ecran, chemin d'image local et donnees d'image brutes, avec retour du texte, de la confiance, des limites rectangulaires et des metadonnees de duree
* `Fonctionnalite` Ajout des variantes de produit `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` et `tamil`
* `Fonctionnalite` Ajout des metadonnees de plugin et instructions d'utilisation localisees en espagnol, francais, russe, arabe, japonais, coreen, anglais, chinois simplifie, chinois traditionnel de Hong Kong et chinois traditionnel de Taiwan
* `Fonctionnalite` Ajout de la generation README et CHANGELOG depuis des sources JSON via `.python/generate_markdown.py`
* `Correctif` Le plugin ne pouvait pas être activé depuis le centre de plugins après son installation sur certains systèmes
* `Amelioration` Uniformiser la mise en page du README et la gestion des versions de la plateforme Gradle
