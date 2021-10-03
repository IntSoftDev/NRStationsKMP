plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

dependencies {
    implementation(project(":sdknrstations"))
    // uncomment the line above and comment out this one when building and publishing
   // implementation(Deps.nrStationsClient)
    implementation(Android.material_x)
    implementation(Android.app_compat_x)
    implementation(Android.swipeLayout)
    implementation(Android.constraintlayout)
    implementation(Deps.napier_logger)
    implementation(Android.lifecycle_viewmodel)
    implementation(Android.lifecycle_livedata)
    implementation(Android.activity_exts)
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
