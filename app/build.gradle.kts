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
        implementation(isdlibs.intsoftdev.stations)
    }

    implementation(isdlibs.compose.material3)
    implementation(isdlibs.androidx.navigation.compose)
    implementation(isdlibs.compose.maps)

    implementation(isdlibs.kmm.viewmodel)

    implementation(isdlibs.bundles.app.ui)
    implementation(isdlibs.multiplatformSettings.common)
    implementation(isdlibs.kotlinx.dateTime)
    implementation(isdlibs.napier.logger)
    implementation(isdlibs.google.gms.maps)
    testImplementation(isdlibs.junit)
}
