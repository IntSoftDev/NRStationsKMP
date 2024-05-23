plugins {
    alias(isdlibs.plugins.androidApplication)
    alias(isdlibs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    @Suppress("UnstableApiUsage")

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    @Suppress("UnstableApiUsage")
    lint {
        warningsAsErrors = false
        abortOnError = true
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    @Suppress("UnstableApiUsage")
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
    implementation(isdlibs.compose.material)
    implementation(isdlibs.compose.ui)
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.4")
    implementation(isdlibs.compose.maps)
    implementation(isdlibs.google.gms.maps)
    implementation(isdlibs.napier.logger)
}
