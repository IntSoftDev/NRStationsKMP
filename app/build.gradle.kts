plugins {
    id("com.android.application")
    kotlin("android")
}

val ISD_API_KEY = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty("ISD_API_KEY")

android {
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.intsoftdev.nrstations.androidApp"
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "ISD_API_KEY", ISD_API_KEY)
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

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(project(":nrstations"))
    // Use the line below for a compiled library rather than source code
    // implementation(isdlibs.intsoftdev.stations)
    implementation(isdlibs.androidx.recyclerview)
    implementation(isdlibs.androidx.swipelayout)
    implementation(isdlibs.androidx.constraintlayout)
    implementation(isdlibs.androidx.lifecycle.livedata)
    implementation(isdlibs.androidx.lifecycle.extensions)
    implementation(isdlibs.androidx.appcompat)
    implementation(isdlibs.bundles.app.ui)
    implementation(isdlibs.multiplatformSettings.common)
    implementation(isdlibs.kotlinx.dateTime)
    implementation(isdlibs.napier.logger)
    coreLibraryDesugaring(isdlibs.android.desugaring)
    implementation(isdlibs.koin.android)
    testImplementation(isdlibs.junit)
}
