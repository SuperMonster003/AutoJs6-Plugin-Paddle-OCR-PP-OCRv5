<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <img src="{{ repo_url }}/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="{{ icon_alt }}" border="0" width="128" />
    </picture>
  </p>

  <p>{{ text_plugin_synopsis }}</p>

  <p>
    <a href="{{ repo_url }}/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/{{ repo_slug }}?label=Release"/></a>
    <a href="{{ repo_url }}/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/{{ repo_slug }}?color=A24232&label=Issues"/></a>
    <a href="{{ license_url }}"><img alt="GitHub License" src="https://img.shields.io/github/license/{{ repo_slug }}?color=534BAE&label=License"/></a>
  </p>
</div>

******

### {{ h3_languages_with_ascii }}

******

{{ p_languages_all_supported_for_readme }}:

{{ placeholder_ul_languages_all_supported }}

******

### {{ h3_introduction }}

******

{{ p_introduction }}

******

### {{ h3_functions }}

******

{{ placeholder_features }}

******

### {{ h3_profiles }}

******

{{ p_profiles_intro }}:

{{ placeholder_profiles }}

******

### {{ h3_usage }}

******

{{ p_usage_screenshot }}:

```js
let capt = images.captureScreen();
ocr.paddle.recognizeText(capt);

ocr.paddle();
```

{{ p_usage_file }}:

```js
let img = images.read("test.png");
ocr.paddle.recognizeText(img);

ocr.paddle("test.png");
```

{{ p_usage_docs }}.

******

### {{ h3_prepare_models }}

******

```powershell
{{ prepare_models_command }}
```

{{ placeholder_prepare_notes }}

******

### {{ h3_release_history }}

******

{{ placeholder_latest_release_history }}

##### {{ h5_for_more_release_history }}

* {{ placeholder_read_more_in_changelog_md }}

******

### {{ h3_build }}

******

```powershell
.\gradlew.bat :app:assembleMobileDebug
.\gradlew.bat :app:assembleServerDebug
```

{{ text_build_all_profiles }}:

```powershell
.\gradlew.bat :app:assembleDebug
```

{{ text_release_build }}:

```powershell
.\gradlew.bat :app:assembleRelease
```

{{ p_build_params }}.

******

### {{ h3_resource_layout }}

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

{{ p_resource_layout }}.

******

### {{ h3_links }}

******

- {{ text_link_autojs6_ocr_docs }}: {{ docs_ocr_url }}
- {{ text_link_paddleocr_official }}: {{ paddleocr_url }}
- {{ text_link_model_source }}: {{ model_source_url }}
- {{ text_link_onnxruntime_android }}: {{ onnxruntime_android_url }}


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Paddle-OCR-PP-OCRv5/blob/master/docs/16kb.md)
