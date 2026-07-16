<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
  </p>

  <p>Plugin de reconnaissance de texte Paddle OCR base sur PP-OCRv5</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/commit/96c9b45bc90d5ac5516e6bb1f10848c628418dac"><img alt="Created" src="https://img.shields.io/date/1773539058?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues

******

Le README.md actuel prend en charge les langues suivantes:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-en.md)
- Français [fr] # actuel
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.readme/README-ar.md)

******

### Introduction

******

Le plugin AutoJs6 Paddle OCR PP-OCRv5 fournit a AutoJs6 la detection de texte et la reconnaissance de texte basees sur PaddleOCR ONNX Runtime. Il prend en charge le modele mobile par defaut, le modele serveur haute precision et les modeles de reconnaissance multilingues.

******

### Fonctionnalites

******

- Fournit le service de plugin `paddle-ocr` avec l'ID de plugin par defaut `paddle-ocr-pp-ocrv5`.
- Prend en charge les appels AutoJs6 comme `ocr.paddle.recognizeText(...)` et `ocr.paddle(...)`.
- Prend en charge les captures d'ecran, les chemins d'images locales et les donnees d'image brutes, avec retour du texte reconnu, de la confiance, des limites rectangulaires et des coordonnees quadrilaterales.
- Fournit les variantes de produit `mobile`, `server`, `english`, `korean`, `latin`, `eslav`, `thai`, `greek`, `arabic`, `cyrillic`, `devanagari`, `telugu` et `tamil`.
- Les metadonnees du plugin, les instructions d'utilisation, le README et le CHANGELOG sont localises en espagnol, francais, russe, arabe, japonais, coreen, anglais, chinois simplifie, chinois traditionnel de Hong Kong et chinois traditionnel de Taiwan.

******

### Profils De Modele

******

Les flavors Gradle et profils de modele actuels incluent:

- `mobile`: profil par defaut utilisant `PP-OCRv5_mobile_det` et `PP-OCRv5_mobile_rec`.
- `server`: profil haute precision utilisant `PP-OCRv5_server_det` et `PP-OCRv5_server_rec`.
- `english`/`korean`/`latin`/`eslav`/`thai`/`greek`/`arabic`/`cyrillic`/`devanagari`/`telugu`/`tamil`: reutilise `PP-OCRv5_mobile_det` et utilise le modele de reconnaissance multilingue PP-OCRv5 correspondant.

******

### Utilisation

******

Reconnaitre le contenu texte d'une capture d'ecran:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

Reconnaitre le contenu texte d'un fichier image local:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

Pour plus d'exemples d'utilisation, consultez la section [Optical Character Recognition (OCR)](https://docs.autojs6.com/#/ocr) de la documentation AutoJs6.

******

### Preparer Les Modeles

******

```powershell
python scripts\prepare_ppocrv5_assets.py --profile all
```

- Le script telecharge les paquets tar ONNX officiels depuis la source des modeles Paddle et copie `inference.onnx` ainsi que `inference.yml` dans les assets de chaque flavor.
- Utilisez des options comme `--profile mobile` pour ne preparer qu'un seul profil de modele.
- Le modele de detection mobile est partage via `app/src/sharedMobileDet/assets`.

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

##### Pour plus d'historique des versions

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/.changelog/CHANGELOG-fr.md)

******

### Compilation

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

Compiler tous les profils:

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilation Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Les parametres de compilation viennent de `version.properties`; le SDK minimum actuel est 24 et le SDK cible est 36.

******

### Structure Des Ressources

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contient les descriptions localisees du plugin; `plugin_instruction.md` contient les instructions d'utilisation affichees par l'hote. README et CHANGELOG sont generes depuis des sources JSON par `.python/generate_markdown.py`, et la racine du depot ne conserve que `README.md`.

******

### Liens

******

- Documentation OCR AutoJs6: https://docs.autojs6.com/#/ocr
- Projet officiel PaddleOCR: https://github.com/PaddlePaddle/PaddleOCR
- Source des modeles ONNX PP-OCRv5: https://paddle-model-ecology.bj.bcebos.com/paddlex/official_inference_model/paddle3.0.0
- Documentation ONNX Runtime Android: https://onnxruntime.ai/docs/tutorials/mobile/deploy-android.html
