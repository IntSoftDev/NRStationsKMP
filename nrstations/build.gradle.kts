import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(isdlibs.plugins.maven.publish)
    alias(isdlibs.plugins.kotlinMultiplatform)
    alias(isdlibs.plugins.cocoapods)
    alias(isdlibs.plugins.kotlin.serialization)
    alias(isdlibs.plugins.android.kotlin.multiplatform.library)
    alias(isdlibs.plugins.sqlDelight)
    alias(isdlibs.plugins.ksp)
    alias(isdlibs.plugins.kmpNativeCoroutines)
}

group = "com.intsoftdev"
version = "1.0.0-ALPHA-16"

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = group.toString(),
        artifactId = "nrstations",
        version = version.toString()
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("NRStations KMP library")
        description.set("Multiplatform SDK to retrieve all the national rail stations in the UK")
        url.set("https://github.com/IntSoftDev/NRStationsKMP")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("azaka01")
                name.set("A Zaka")
                email.set("az@intsoftdev.com")
            }
        }
        scm {
            url.set("https://github.com/IntSoftDev/NRStationsKMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
        namespace = "com.intsoftdev.nrstations"
        compileSdk = isdlibs.versions.compileSdk.get().toInt()
        minSdk = isdlibs.versions.minSdk.get().toInt()

        // Kotlin JVM target for Android compilations
        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_17
                )
                // Expect/actual classes warning suppression
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }

        // Tests are disabled by default with this plugin; enable if you need them:
        withHostTestBuilder {}           // enables androidHostTest (Robolectric-style)
        withDeviceTestBuilder {
            // map to "test" tree
            sourceSetTreeName = "test"
        }
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

        androidMain {
            dependencies {
                api(isdlibs.koin.android)
                implementation(isdlibs.androidx.lifecycle.viewmodel)
                implementation(isdlibs.sqlDelight.android)
                implementation(isdlibs.ktor.client.okHttp)
            }
        }

        iosMain {
            dependencies {
                implementation(isdlibs.sqlDelight.native)
                implementation(isdlibs.ktor.client.ios)
            }
        }

        commonMain {
            dependencies {
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
        }

        commonTest {
            dependencies {
                implementation(isdlibs.bundles.commonTest)
                implementation(isdlibs.turbine)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(isdlibs.bundles.androidTest)
            }
        }
    }

    cocoapods {
        summary = "KMP Stations"
        homepage = "https://github.com/intsoftdev/NRStationsKMP"
        framework {
            isStatic = false // SwiftUI preview requires dynamic framework
        }
        ios.deploymentTarget = "16.0"
        podfile = project.file("../ios/Podfile")

        // Maps custom Xcode configuration to NativeBuildType
        // https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-cocoapods-overview.html#configure-the-project
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
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
