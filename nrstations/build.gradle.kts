plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("kotlin-parcelize")
    id("convention.publication")
    `maven-publish`
}

android {
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    ios()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
    iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(isdlibs.koin.core)
                implementation(isdlibs.coroutines.core)
                implementation(isdlibs.sqlDelight.coroutinesExt)
                implementation(isdlibs.bundles.ktor.common)
                implementation(isdlibs.touchlab.stately)
                implementation(isdlibs.multiplatformSettings.common)
                implementation(isdlibs.kotlinx.dateTime)
                implementation(isdlibs.kolinx.serialization)
                implementation(isdlibs.bundles.ktor.common)
                implementation(isdlibs.napier.logger)
                api(isdlibs.moko.parcelize)
                api(isdlibs.touchlab.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(isdlibs.bundles.sdknrstations.commonTest)
                implementation("com.russhwolf:multiplatform-settings-test:1.0.0-RC")
            }
        }
        val androidMain by getting {
            dependencies {
                api(isdlibs.koin.android)
                implementation(isdlibs.androidx.lifecycle.viewmodel)
                implementation(isdlibs.sqlDelight.android)
                implementation(isdlibs.ktor.client.okHttp)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(isdlibs.bundles.sdknrstations.androidTest)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(isdlibs.sqlDelight.native)
                implementation(isdlibs.ktor.client.ios)
            }
        }
        val iosTest by getting
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }

    sourceSets.matching { it.name.endsWith("Test") }
        .configureEach {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }

    cocoapods {
        summary = "KMP Stations"
        homepage = "https://github.com/intsoftdev/NRStationsKMP"
        framework {
            isStatic = false // SwiftUI preview requires dynamic framework
        }
        ios.deploymentTarget = "12.4"
        podfile = project.file("../ios/Podfile")
    }
}

sqldelight {
    database("NRStationsDb") {
        packageName = "com.intsoftdev.nrstations.database"
    }
}