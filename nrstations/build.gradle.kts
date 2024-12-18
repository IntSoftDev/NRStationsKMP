import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(isdlibs.plugins.kotlinMultiplatform)
    alias(isdlibs.plugins.cocoapods)
    alias(isdlibs.plugins.kotlin.serialization)
    alias(isdlibs.plugins.androidLibrary)
    alias(isdlibs.plugins.sqlDelight)
    id("convention.publication")
    alias(isdlibs.plugins.ksp)
    alias(isdlibs.plugins.kmpNativeCoroutines)
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.intsoftdev.nrstations"
    compileSdk = isdlibs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = isdlibs.versions.minSdk.get().toInt()
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        /**
         * Lint found an issue registry (androidx.lifecycle.lint.LiveDataCoreIssueRegistry)
         * which contains some references to invalid API
         */
        disable.add("ObsoleteLintCustomCheck")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    // https://kotlinlang.org/docs/multiplatform-expect-actual.html#expected-and-actual-classes
    // To suppress this warning about usage of expected and actual classes
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        @Suppress("OPT_IN_USAGE")
        unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        /**
         * error when enabling Xallocator
         * Task :nrstations:linkPodDebugFrameworkIosArm64 FAILED
         * dependency not cached /Users//.konan/kotlin-native-prebuilt-macos-x86_64-2.0.0/klib/common/stdlib
         * iosTarget.compilations.configureEach {
         *             compileTaskProvider.configure {
         *                 compilerOptions {
         *                      freeCompilerArgs.add("-Xallocator=mimalloc")
         *                 }
         *             }
         *         }
         */
        iosTarget.binaries.framework {
            baseName = "nrstations"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }

        androidMain.dependencies {
            api(isdlibs.koin.android)
            implementation(isdlibs.androidx.lifecycle.viewmodel)
            implementation(isdlibs.sqlDelight.android)
            implementation(isdlibs.ktor.client.okHttp)
        }

        iosMain.dependencies {
            implementation(isdlibs.sqlDelight.native)
            implementation(isdlibs.ktor.client.ios)
        }

        commonMain.dependencies {
            implementation(isdlibs.koin.core)
            implementation(isdlibs.coroutines.core)
            implementation(isdlibs.sqlDelight.coroutinesExt)
            implementation(isdlibs.bundles.ktor.common)
            implementation(isdlibs.multiplatformSettings.common)
            implementation(isdlibs.kotlinx.dateTime)
            implementation(isdlibs.bundles.ktor.common)
            implementation(isdlibs.napier.logger)
            api(isdlibs.kmp.viewmodel)
        }

        commonTest.dependencies {
            implementation(isdlibs.bundles.commonTest)
            implementation(isdlibs.turbine)
        }

        getByName("androidUnitTest").dependencies {
            implementation(isdlibs.bundles.androidTest)
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
    databases {
        create("NRStationsDb") {
            packageName.set("com.intsoftdev.nrstations.database")
        }
    }
}
