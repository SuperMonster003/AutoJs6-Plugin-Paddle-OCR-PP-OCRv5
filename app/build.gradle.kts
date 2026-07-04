import com.android.build.api.variant.FilterConfiguration
import org.gradle.api.provider.Property

plugins {
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.paddleocr.v5"

val buildTypeDebug = "debug"
val buildTypeRelease = "release"

android {

    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        applicationId = globalApplicationId

        minSdk = 26
        targetSdk = versions.sdkVersionTarget

        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "VERSION_DATE", "\"${utils.getDateString("MMM d, yyyy", "GMT+08:00")}\"")
        buildConfigField("String", "PLUGIN_ENGINE", "\"paddle-ocr\"")
        buildConfigField("String", "PLUGIN_VARIANT", "\"v5\"")
        resValue("string", "plugin_author", "SuperMonster003")
        resValue("string", "plugin_engine", "paddle-ocr")
        resValue("string", "plugin_variant", "v5")
        resValue("string", "plugin_version_date", utils.getDateString("MMM d, yyyy", "GMT+08:00"))

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
    }

    flavorDimensions += "ocrProfile"

    productFlavors {
        create("mobile") {
            dimension = "ocrProfile"
            versionNameSuffix = "-mobile"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5\"")
            buildConfigField("String", "MODEL_PROFILE", "\"mobile\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Mobile)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5")
            resValue("string", "model_profile", "Mobile")
        }

        create("server") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".server"
            versionNameSuffix = "-server"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-server\"")
            buildConfigField("String", "MODEL_PROFILE", "\"server\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Server)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-server")
            resValue("string", "model_profile", "Server")
        }

        create("english") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".english"
            versionNameSuffix = "-english"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-english\"")
            buildConfigField("String", "MODEL_PROFILE", "\"english\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 English)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-english")
            resValue("string", "model_profile", "English")
        }

        create("korean") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".korean"
            versionNameSuffix = "-korean"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-korean\"")
            buildConfigField("String", "MODEL_PROFILE", "\"korean\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Korean)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-korean")
            resValue("string", "model_profile", "Korean")
        }

        create("latin") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".latin"
            versionNameSuffix = "-latin"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-latin\"")
            buildConfigField("String", "MODEL_PROFILE", "\"latin\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Latin)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-latin")
            resValue("string", "model_profile", "Latin")
        }

        create("eslav") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".eslav"
            versionNameSuffix = "-eslav"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-eslav\"")
            buildConfigField("String", "MODEL_PROFILE", "\"eslav\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Eslav)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-eslav")
            resValue("string", "model_profile", "Eslav")
        }

        create("thai") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".thai"
            versionNameSuffix = "-thai"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-thai\"")
            buildConfigField("String", "MODEL_PROFILE", "\"thai\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Thai)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-thai")
            resValue("string", "model_profile", "Thai")
        }

        create("greek") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".greek"
            versionNameSuffix = "-greek"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-greek\"")
            buildConfigField("String", "MODEL_PROFILE", "\"greek\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Greek)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-greek")
            resValue("string", "model_profile", "Greek")
        }

        create("arabic") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".arabic"
            versionNameSuffix = "-arabic"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-arabic\"")
            buildConfigField("String", "MODEL_PROFILE", "\"arabic\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Arabic)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-arabic")
            resValue("string", "model_profile", "Arabic")
        }

        create("cyrillic") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".cyrillic"
            versionNameSuffix = "-cyrillic"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-cyrillic\"")
            buildConfigField("String", "MODEL_PROFILE", "\"cyrillic\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Cyrillic)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-cyrillic")
            resValue("string", "model_profile", "Cyrillic")
        }

        create("devanagari") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".devanagari"
            versionNameSuffix = "-devanagari"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-devanagari\"")
            buildConfigField("String", "MODEL_PROFILE", "\"devanagari\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Devanagari)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-devanagari")
            resValue("string", "model_profile", "Devanagari")
        }

        create("telugu") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".telugu"
            versionNameSuffix = "-telugu"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-telugu\"")
            buildConfigField("String", "MODEL_PROFILE", "\"telugu\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Telugu)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-telugu")
            resValue("string", "model_profile", "Telugu")
        }

        create("tamil") {
            dimension = "ocrProfile"
            applicationIdSuffix = ".tamil"
            versionNameSuffix = "-tamil"
            buildConfigField("String", "PLUGIN_ID", "\"paddle-ocr-pp-ocrv5-tamil\"")
            buildConfigField("String", "MODEL_PROFILE", "\"tamil\"")
            resValue("string", "app_name", "Paddle OCR (PP-OCRv5 Tamil)")
            resValue("string", "plugin_id", "paddle-ocr-pp-ocrv5-tamil")
            resValue("string", "model_profile", "Tamil")
        }
    }

    lint {
        abortOnError = false
    }

    signingConfigs {
        if (signs.isValid) {
            create(buildTypeRelease) {
                storeFile = signs.properties["storeFile"]?.let { file(it as String) }
                keyPassword = signs.properties["keyPassword"] as String
                keyAlias = signs.properties["keyAlias"] as String
                storePassword = signs.properties["storePassword"] as String
            }
        }
    }

    buildTypes {
        val proguardFiles = arrayOf<Any>(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro",
        )
        val niceSigningConfig = takeIf { signs.isValid }?.let {
            signingConfigs.getByName(buildTypeRelease)
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
        release {
            isMinifyEnabled = true
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
        resValues = true
    }

    sourceSets.named("main") {
        kotlin.directories += "src/main/java"
    }

    listOf(
        "mobile",
        "english",
        "korean",
        "latin",
        "eslav",
        "thai",
        "greek",
        "arabic",
        "cyrillic",
        "devanagari",
        "telugu",
        "tamil",
    ).forEach { profile ->
        sourceSets.named(profile) {
            assets.srcDir("src/sharedMobileDet/assets")
        }
    }

    // @Hint by SuperMonster003 on Sep 25, 2024.
    //  ! To maintain compatibility with lower versions of Gradle (such as 7.4.2).
    //  ! zh-CN: 为了兼容低版本 Gradle (如 7.4.2).
    //  # packaging { ... }
    @Suppress("DEPRECATION")
    packagingOptions {
        jniLibs.useLegacyPackaging = true

        listOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.*",
            "META-INF/LICENSE-notice.*",
            "META-INF/license.*",
            "META-INF/NOTICE",
            "META-INF/NOTICE.*",
            "META-INF/notice.*",
            "META-INF/ASL2.0",
            "META-INF/*.kotlin_module",
        ).let { resources.pickFirsts.addAll(it) }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a")
            isUniversalApk = true
        }
    }

    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = false
        }
        abi {
            enableSplit = false
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val architecture = output.filters.find {
                it.filterType == FilterConfiguration.FilterType.ABI
            }?.identifier ?: "universal"
            val outputFileNameProperty = output.javaClass.methods.firstOrNull {
                it.name == "getOutputFileName" && it.parameterTypes.isEmpty()
            }?.invoke(output) as? Property<*>

            @Suppress("UNCHECKED_CAST")
            (outputFileNameProperty as? Property<String>)?.set(
                output.versionName.map { versionName ->
                    val version = versionName.replace("\\s".toRegex(), "-")
                    val extension = utils.FILE_EXTENSION_APK
                    "${rootProject.name}-v$version-$architecture.$extension".lowercase()
                }
            )
        }
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.21")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:2.2.21")

    implementation("org.jetbrains:annotations:26.0.2")

    implementation(files("$rootDir/libs/common-plugin-api.aar"))

    implementation(files("$rootDir/libs/paddle-ocr-api.aar"))

    implementation(project(":libs:ppocrv5-plugin-runtime"))
    implementation(project(":libs:ppocr-android-sdk"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation(libs.core.ktx)
    implementation(libs.annotation.jvm)
}

tasks {
    withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
    }

    register<Copy>("appendDigestToReleasedFiles") {
        val src = buildTypeRelease
        val dst = "${src}s"
        val ext = utils.FILE_EXTENSION_APK

        if (!file(src).isDirectory) {
            return@register
        }

        from(src); into(dst); include("*.$ext")

        rename { name ->
            utils.digestCRC32(file("${src}/$name")).let { digest ->
                name.replace(Regex("^(.+?)(\\.$ext)$"), "$1-$digest$2")
            }
        }

        doLast { println("Destination: ${file(dst)}") }
    }
}

extra {
    versions.handleIfNeeded(project, listOf(buildTypeDebug, buildTypeRelease))
}
