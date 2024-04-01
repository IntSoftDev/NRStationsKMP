plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.androidApp"
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    lint {
        warningsAsErrors = false
        abortOnError = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = isdlibs.versions.composeCompiler.get()
    }
}

secrets {
    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

val IMPORT_LOCAL_NRSTATIONS_KMP: String by project

dependencies {
    // Import NRStations KMP as local dependency
    if (IMPORT_LOCAL_NRSTATIONS_KMP == "true") {
        implementation(project(":nrstations"))
    } else {
        // use build from Maven Central
        implementation("com.intsoftdev:nrstations:1.0.0-ALPHA-2")
    }

    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material:1.6.4")
    implementation("androidx.compose.ui:ui:1.6.4")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.4")
    implementation("com.google.maps.android:maps-compose:2.7.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("io.github.aakira:napier:2.6.1")
}
