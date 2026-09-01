// @Hint: declared here, applied by the modules.
//  ! These plugins used to arrive through the settings buildscript classpath, which the
//  ! platform-versions plugin replaces. Declaring them once here keeps the version in a
//  ! single place and leaves the module scripts untouched, which Groovy modules require:
//  ! their plugins block accepts string literals only, so a computed version cannot go there.
//  ! zh-CN: 这些插件原先经由 settings buildscript classpath 提供, 现已被 platform-versions 插件取代.
//  ! 在此声明一次可使版本只出现在一处, 且模块脚本无须改动 -- 这对 Groovy 模块是必需的:
//  ! 它们的 plugins 块只接受字符串字面量, 无法写入运行时计算出的版本.
plugins {
    id("com.android.application") version System.getProperty("gradle.agp.version") apply false
    id("com.android.library") version System.getProperty("gradle.agp.version") apply false
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/public")
    }
}

tasks {
    register<Delete>("clean").configure {
        // @Legacy delete(rootProject.buildDir)
        delete(rootProject.layout.buildDirectory)
    }
}
