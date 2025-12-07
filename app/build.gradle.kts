plugins {
    alias(isdlibs.plugins.androidApplication)
    alias(isdlibs.plugins.kotlin.android)
    alias(isdlibs.plugins.secrets.gradle)
    alias(isdlibs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.intsoftdev.nrstations.app"
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.app"
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
        disable.add("ObsoleteLintCustomCheck")
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

secrets {
    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

val importLocalStationsKmp: String by project

dependencies {
    // Import NRStations KMP as local dependency
    if (importLocalStationsKmp == "true") {
        implementation(project(":nrstations"))
    } else {
        // use build from Maven Central
        implementation(isdlibs.intsoftdev.stations)
    }

    implementation(isdlibs.compose.material3)
    implementation(isdlibs.androidx.navigation.compose)
    implementation(isdlibs.compose.material.icons.extended)
    implementation(isdlibs.compose.ui)
    implementation(isdlibs.compose.tooling)
    implementation(isdlibs.compose.maps)
    implementation(isdlibs.google.gms.maps)
    implementation(isdlibs.napier.logger)
}
