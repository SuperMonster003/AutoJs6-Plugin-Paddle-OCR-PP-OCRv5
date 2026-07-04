plugins {
    id("org.autojs.build.versions")
    id("org.autojs.build.jvm-convention")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.github.supermonster003.autojs6.plugin.paddleocr.v5.runtime"
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("proguard-rules.pro")
    }

    lint {
        targetSdk = versions.sdkVersionTarget
        abortOnError = false
    }

    buildFeatures {
        buildConfig = false
    }

    sourceSets.named("main") {
        kotlin.directories += "src/main/java"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    compileOnly(files("$rootDir/libs/common-plugin-api.aar"))
    compileOnly(files("$rootDir/libs/paddle-ocr-api.aar"))
    implementation(project(":libs:ppocr-android-sdk"))
}
