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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
    }
}

dependencies {
    implementation(project(":sdknrstations"))
    // uncomment the line above and comment out this one when building and publishing
   // implementation(Deps.nrStationsClient)
    implementation(Deps.material_x)
    implementation(Deps.app_compat_x)
    implementation(Deps.swipeLayout)
    implementation(Deps.constraintlayout)
    implementation(Deps.napier_logger)
    implementation(Deps.lifecycle_viewmodel)
    implementation(Deps.lifecycle_livedata)
    implementation(Deps.activity_exts)
    implementation(Ktor.androidCore)
    implementation(Koin.koinCore)
    implementation(Koin.android)
    implementation(Deps.multiplatformSettings)
    implementation(Coroutines.android)
}
