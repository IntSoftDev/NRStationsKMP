plugins {
    id("com.android.application")
    kotlin("android")
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
    packagingOptions {
        resources.excludes.add("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = isdlibs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":sdknrstations"))
    // Use the line below for a compiled library rather than source code
    // implementation(isdlibs.intsoftdev.stations)
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(isdlibs.androidx.appcompat)
    implementation(isdlibs.bundles.app.ui)
    implementation(isdlibs.multiplatformSettings.common)
    implementation(isdlibs.kotlinx.dateTime)
    implementation(isdlibs.napier.logger)
    coreLibraryDesugaring(isdlibs.android.desugaring)
    implementation(isdlibs.koin.android)
    testImplementation(isdlibs.junit)
}
