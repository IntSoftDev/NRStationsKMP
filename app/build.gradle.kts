plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

// TODO add BuildConfig and integrate with GHA
// val ISD_API_KEY = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty("ISD_API_KEY")

android {
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.androidApp"
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // TODO add BuildConfig
        // buildConfigField("String", "ISD_API_KEY", ISD_API_KEY)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = isdlibs.versions.composeCompiler.get()
    }
}

kotlin {
    jvmToolchain(11)
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
    // extra compose dependencies
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
