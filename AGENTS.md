# AutoJs6-Plugin-Paddle-OCR-PP-OCRv5: repository rules

These rules apply to this existing plugin. They implement the workspace's AutoJs6 plugin repository specification for its actual Android/native capabilities.

## Working tree and commits

- Read `git status --short`, branch, recent commits and relevant diffs before editing. Preserve pre-existing work and check nested AGENTS.md. Do not stage another task's files or partial changes.
- Use Conventional Commits. Before every commit set `VERSION_BUILD` to `git rev-list --count HEAD` plus one; after committing verify it equals the reachable HEAD count. Do not increment it during builds.
- SemVer describes user-visible compatibility. Update all ten changelog JSON sources for runtime fixes and improvements, then regenerate documentation.
- Local signing material, local.properties, generated APKs and migration backup files are ignored and must never be committed or logged.

## Build and release

- Root project name is `autojs6-plugin-paddle-ocr-pp-ocrv5`. Use the publicly published platform-versions and native-alignment plugins at 1.8.1; no local plugin override, mavenLocal, consumer gradle/data, or sibling JAR/AAR dependency.
- Apply platform-versions in root settings before build-logic. Root Android/Kotlin plugin versions consume the platform's system properties. Use AGP's built-in Kotlin support and UTF-8 compilation.
- SDK, package version and JDK inputs come from version.properties and build-logic. Builds must not rewrite tracked version inputs.
- Python 3.10+ and Android SDK build tools are required for release validation. `:app:appendDigestToReleasedFiles` depends on `assembleRelease` and validates the configured release variant/ABI matrix, actual APK identities, release certificate, 64-bit ELF/ZIP alignment and CRC32 before archiving to `releases/v<VERSION_NAME>/`.
- Keep signed universal and single-ABI outputs for every release flavor. The release helper refuses stale APKs, incomplete matrices, unknown files and unmanaged destination directories. Legacy Android Studio `app/release` and `app/releases` directories are not release inputs.
- The native inventory must agree with getInfo() for the actual installed APK. Do not infer release readiness from source files or a different version's APK.
- One-time upgrade migration: `VERSION_CODE_OFFSET=49`. `VERSION_BUILD` is the Git count; Android `versionCode = VERSION_BUILD + VERSION_CODE_OFFSET` preserves upgrades from historical build numbers. Keep this offset fixed; expose the installed PackageInfo code to the host.

## Activation and metadata

- Services: .PpOcrV5PluginService. Discovery actions: org.autojs.plugin.PADDLE_OCR. Preserve their existing public action/category/AIDL contracts and host capability negotiation.
- Wake Activity must resolve from `org.autojs.plugin.WAKE_ACTIVITY`, be exported, protected by `org.autojs.permission.PLUGIN`, use Theme.NoDisplay, respond to the WAKE action plus DEFAULT category, and finish immediately.
- PluginInfo must expose the English app label, localized description, PackageInfo version, actual APK ABI inventory, plugin identity, build date and minimum host capability.
- Validate input bounds and error propagation; close file descriptors and native resources deterministically. Keep host entry points and public contracts compatible.

## Resources and documentation

- English titles and identity strings are untranslatable. Keep default English and explicit values-en equal, with complete ar/es/fr/ja/ko/ru/zh-Hans/zh-Hant-HK/zh-Hant-TW resources.
- Sort strings by name, separate untranslatable resources and plurals, use ASCII punctuation and `...` with TypographyEllipsis suppression. Descriptions have no terminal punctuation or unnecessary host-specific preamble.
- Maintain the plugin's actual PNG icon at app/src/main/res/mipmap/ic_launcher.png.
- Edit .readme/.changelog JSON and templates, then run the generator and its read-only check. Root README is the same Simplified Chinese source as README-zh-Hans.md. Changelogs are generated under assets/doc.
- Keep third-party provenance/license/hash records synchronized with native/API/model changes. A settings or updater UI must link to bundled localized release history.

## Verification

```powershell
python .python/generate_markdown.py --check
python -m unittest discover -s .python -p test_release_archive.py
.\gradlew.bat --no-daemon --max-workers=2 '-Djava.vendor=Eclipse Adoptium' '-Djava.vendor.version=Temurin-21.0.12.1+1' :app:assembleMobileDebug :app:testMobileDebugUnitTest
.\gradlew.bat --no-daemon --max-workers=2 :app:assembleMobileDebugAndroidTest :app:lintMobileDebug
.\gradlew.bat --no-daemon --max-workers=2 :app:appendDigestToReleasedFiles
```

`Mobile` is the representative Debug build; release validation always checks all configured flavors. Run instrumentation on a compatible host/device for discovery, binding, getInfo, actual native/Binder happy paths, invalid input, rebind and cleanup. Verify every published ABI, including arm64 and x86_64 and a 16 KB device where supported. ColorOS activation and device tests require explicit evidence; compiling tests or checking ELF alignment does not establish a device pass. Record any missing device/flavor coverage in the release assessment.

Image inputs are limited to 16777216 pixels; packed/padded raw buffers are limited to 64 MiB. Dimensions and stride are validated before allocation. Device CI covers x86_64 on 4 KB and 16 KB images where that ABI is available; ARM-only Paddle-Lite requires the explicitly configured ANDROID_ARM64_RUNNER. Missing runner coverage remains a release verification gap.

Encoded images are also capped at 64 MiB. Hosts send non-seekable pipes: consume each descriptor once, then inspect dimensions and decode the retained bytes. Instrumentation must exercise the pipe transport as well as files.
