Utilisez Paddle OCR pour reconnaitre le contenu texte d'une capture d'ecran:

```js
/* Forme complete. */

let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

/* Forme abregee. */

ocr.paddle.recognizeText();

/* Forme minimale. */

ocr.paddle();
```

Utilisez Paddle OCR pour reconnaitre le contenu texte d'un fichier image local (en prenant `test.png` comme exemple):

```js
/* Forme complete. */

let img = images.read("test.png");
ocr.paddle.recognizeText(img);

/* Forme abregee. */

ocr.paddle.recognizeText("test.png");

/* Forme minimale. */

ocr.paddle("test.png");
```

Pour plus de modes d'utilisation, consultez la section [Reconnaissance optique de caracteres (OCR)](https://docs.autojs6.com/#/ocr)
de la documentation AutoJs6.