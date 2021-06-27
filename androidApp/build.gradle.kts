plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

dependencies {
  //  implementation(project(":sdknrstations"))
    // uncomment the line above and comment out this one when building and publishing
    implementation(Deps.nrStationsClient)
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


android {
    compileSdkVersion(Versions.compile_sdk)
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.androidApp"
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
    }
}
