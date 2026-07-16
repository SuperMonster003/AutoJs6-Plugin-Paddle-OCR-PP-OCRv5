plugins {
    id("org.autojs.build.versions")
    id("org.autojs.build.jvm-convention")
    id("com.android.library")
}

android {
    namespace = "com.paddle.ocr"
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
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.21.1")
    implementation(project(":libs:org-opencv-4_8_0"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation(libs.core.ktx)
}
