<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-paddle-ocr-pp-ocrv5-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Plugin de reconnaissance de texte Paddle OCR base sur PP-OCRv5</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5?color=A24232&label=Issues"/></a>
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
- Les images peuvent contenir jusqu'à 16777216 pixels; les tampons bruts sont limités à 64 MiB
- Les images encodées sont limitées à 64 MiB avec prise en charge des fichiers et des tubes

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

# v1.0.4

###### 2026/09/19

* `Correctif` Avertissements de lecture SDK XML v4 avec AGP 9.1 et contrôles d'alignement natif des APK déclenchés par erreur lors de l'assemblage des tests unitaires JVM, avec les plugins de compilation partagés 1.8.3
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

##### Pour plus d'historique des versions

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

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

Les parametres de compilation viennent de `version.properties`; le SDK minimum actuel est 26 et le SDK cible est 37.

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/docs/16kb.md)
