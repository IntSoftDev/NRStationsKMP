plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

group = "com.intsoftdev.nrstations"
version = "1.0-SNAPSHOT"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(Deps.material_x)
    implementation(Deps.app_compat_x)
    implementation(Deps.constraintlayout)
    implementation(Deps.napier_logger)
    implementation(Deps.lifecycle_viewmodel)
    implementation(Deps.lifecycle_livedata)
    implementation(Deps.activity_exts)
    implementation(Ktor.androidCore)
    implementation(Koin.koinCore)
    implementation(Koin.android)
    implementation(Koin.androidViewModel)
    implementation(Coroutines.common)
    implementation(Coroutines.android)
}
