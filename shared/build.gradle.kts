import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}
group = "com.intsoftdev.nrstations"
version = "1.0-SNAPSHOT"

android {
    compileSdkVersion(Versions.compile_sdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(SqlDelight.runtime)
                implementation(Koin.koinCore)
                implementation(Ktor.commonCore)
                implementation(Ktor.commonJson)
                implementation(Ktor.commonLogging)
                implementation(Ktor.commonSerialization)
                implementation(Deps.napier_logger)
                implementation(Deps.multiplatformSettings)
                implementation(Coroutines.common){
                    version {
                        strictly(Versions.coroutines)
                    }
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(KotlinTest.common)
                implementation(KotlinTest.annotations)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(SqlDelight.driverAndroid)
                implementation(Deps.material_x)
                implementation(Coroutines.android)
                implementation(Ktor.androidSerialization)
                implementation(Ktor.androidCore)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(KotlinTest.junit)
                implementation(Deps.junit)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(SqlDelight.driverIos)
                implementation(Koin.koinCore)
                implementation(Ktor.ios)
                implementation(Deps.napier_logger)
                implementation(Coroutines.common){
                    version {
                        strictly(Versions.coroutines)
                    }
                }
            }
        }
        val iosTest by getting
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)


sqldelight {
    database("NRStationsKMPDb") {
        packageName = "com.intsoftdev.nrstations.db"
    }
}