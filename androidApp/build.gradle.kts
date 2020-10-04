plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}
group = "com.intsoftdev.nrstations"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
dependencies {
    implementation(project(":shared"))
    implementation(Deps.material_x)
    implementation(Deps.app_compat_x)
    implementation(Deps.constraintlayout)
}
android {
    compileSdkVersion(Versions.compile_sdk)
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.androidApp"
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}