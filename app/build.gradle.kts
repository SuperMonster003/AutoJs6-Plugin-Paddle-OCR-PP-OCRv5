import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.parcelize")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.paddleocr.v5"

val buildTypeDebug = "debug"
val buildTypeRelease = "release"

android {

    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        applicationId = applicationId

        minSdk = versions.sdkVersionMin
        targetSdk = versions.sdkVersionTarget

        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        buildConfigField("String", "VERSION_DATE", "\"${utils.getDateString("MMM d, yyyy", "GMT+08:00")}\"")

        ndk {
            // noinspection ChromeOsAbiSupport
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
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
            getDefaultProguardFile("proguard-android.txt"),
            "proguard-rules.pro",
        )
        val niceSigningConfig = takeIf { signs.isValid }?.let {
            signingConfigs.getByName(buildTypeRelease)
        }
        debug {
            isMinifyEnabled = getByName(buildTypeRelease).isMinifyEnabled
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
    }

    // @Hint by SuperMonster003 on Sep 25, 2024.
    //  ! To maintain compatibility with lower versions of Gradle (such as 7.4.2).
    //  ! zh-CN: 为了兼容低版本 Gradle (如 7.4.2).
    //  # packaging { ... }
    @Suppress("DEPRECATION")
    packagingOptions {
        jniLibs.useLegacyPackaging = false

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

        listOf(
            "com/**/*",
            "frameworks/**/*",
            "junit/**/*",
            "LICENSE-junit.txt",
            "spec.txt",
            "EmojiReference.txt",
        ).let { resources.excludes.addAll(it) }
    }

    applicationVariants.all {
        outputs.map { it as BaseVariantOutputImpl }.forEach {
            it.outputFileName = run {
                val variant = this@all as ApplicationVariantImpl
                val name = rootProject.name
                val version = variant.versionName.replace("\\s".toRegex(), "-") // e.g. 0.1.0
                val architecture = it.getFilter("ABI") ?: "universal"
                val extension = utils.FILE_EXTENSION_APK
                "$name-v$version-$architecture.$extension".lowercase()
            }
        }
    }

    splits {
        // Configures multiple APKs based on ABI.
        abi {
            // Enables building multiple APKs per ABI.
            isEnable = true
            // By default, all ABIs are included, so use reset() and include to specify that we only
            // want APKs for x86 and x86_64.
            // Resets the list of ABIs that Gradle should create APKs for to none.
            reset()
            // Specifies a list of ABIs that Gradle should create APKs for.
            include("arm64-v8a", "armeabi-v7a")
            // Specifies that we do not want to also generate a universal APK that includes all ABIs.
            isUniversalApk = true
        }
    }
}

dependencies {

    // Plugin API: OCR API
    implementation(files("$rootDir/libs/paddle-ocr-api.aar"))

    // Plugin API: OCR Engine
    implementation(files("$rootDir/libs/paddle-ocr-engine.aar"))

    // PaddleOCR
    implementation(project(":libs:paddleocr"))

    // AndroidX Annotations
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
