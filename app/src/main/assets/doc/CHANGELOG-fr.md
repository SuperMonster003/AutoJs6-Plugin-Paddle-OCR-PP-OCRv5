******

### Historique Des Versions

******

# v1.0.4

###### 2026/09/15

* `Amelioration` compileSdk et targetSdk passent à 37 (Android 17) ; le comportement du plugin ne dépend pas de la nouvelle cible

# v1.0.3

###### 2026/09/13

* `Correctif` Les informations de version et d'ABI du centre des plugins correspondent à l'APK installé
* `Correctif` Les images encodées sont limitées à 64 MiB avec prise en charge des fichiers et des tubes
* `Correctif` Les dates de version utilisent un format anglais uniforme
* `Amelioration` Validation des versions, signatures et variantes complètes des APK avant la création des fichiers à télécharger
* `Amelioration` Les images peuvent contenir jusqu'à 16777216 pixels; les tampons bruts sont limités à 64 MiB
* `Amelioration` Étendre les ABI natives et les métadonnées du plugin à arm64-v8a, armeabi-v7a, x86 et x86_64, avec des APK universels et par ABI cohérents

# v1.0.2

###### 2026/09/12

* `Correctif` Correction du plantage du moteur à l'initialisation (SIGSEGV) sur les appareils à pages de 16 Ko : la `libc++_shared.so` embarquée est désormais la version compilée avec le NDK r28.2, dont le segment RELRO ne partage plus de page avec des données inscriptibles (arm64-v8a, armeabi-v7a)
* `Correctif` Correction des modèles de reconnaissance volumineux renvoyant silencieusement un résultat vide après un `OutOfMemoryError` : les modèles sont copiés une seule fois dans le stockage privé de l'application et mappés en mémoire par ONNX Runtime au lieu d'être lus dans le tas Java
* `Correctif` Échecs de compilation avec certaines combinaisons Gradle/AGP dus aux sources Kotlin non compilées ou aux définitions en double de `WakeActivity`
* `Amelioration` Bibliothèque native OpenCV 4.8.0 synchronisée avec la recompilation NDK r28c (Clang 19.0.1) (donneur : AutoJs6-Plugin-OpenCV) ; `libopencv_java4.so` des 4 ABI conserve l'alignement `PT_LOAD` de 16 Ko et embarque un manifeste de provenance

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
