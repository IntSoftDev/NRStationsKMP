
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("convention.publication")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
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

    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    ios()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
    iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
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
                implementation(isdlibs.multiplatformSettings.common)
                implementation(isdlibs.kotlinx.dateTime)
                implementation(isdlibs.kolinx.serialization)
                implementation(isdlibs.bundles.ktor.common)
                implementation(isdlibs.napier.logger)
                implementation(isdlibs.kmm.viewmodel)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(isdlibs.bundles.commonTest)
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
        val androidUnitTest by getting {
            dependencies {
                implementation(isdlibs.bundles.androidTest)
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
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlin.experimental.ExperimentalObjCName")
            }
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

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}

sqldelight {
    database("NRStationsDb") {
        packageName = "com.intsoftdev.nrstations.database"
    }
}
