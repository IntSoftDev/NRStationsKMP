import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

buildscript {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(Deps.kotlin_gradle_plugin)
        classpath(Deps.android_gradle_plugin)
        classpath(Deps.jfrog_gradle_plugin)
    }
}

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    kotlin("plugin.serialization") version "1.4.30"
    `maven-publish`
    id("com.jfrog.bintray") version Versions.jfrog_bintray_plugin
}

android {
    compileSdkVersion(Versions.compile_sdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    // workaround for https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug") // TODO or should it be just  publishLibraryVariants
    }

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "sdknrstations"
            }
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(Deps.kotlinx_serialization)
                implementation(Ktor.commonCore)
                implementation(Ktor.commonJson)
                implementation(Ktor.commonLogging)
                implementation(Ktor.commonSerialization)
                implementation(Deps.multiplatformSettings)
                implementation(Deps.kotlinxDateTime)
                implementation(Coroutines.common)
                // Kodein-DB
                api(KodeinDb.kodeinDb)
                api(KodeinDb.kodeinSerializer)

                // kermit
                api(Deps.kermit)

                // Koin DI
                api(Koin.koinCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(KotlinTest.common)
                implementation(KotlinTest.annotations)
                implementation(Deps.multiplatformSettingsTest)
                // Karmok is a touchLab experimental library which helps with mocking interfaces
                implementation(Deps.karmok)
                implementation(Deps.turbine)
            }
        }

        val androidMain by getting {
            dependencies {
                api(Koin.android)
                implementation(Deps.material_x)
                implementation(Coroutines.android)
                implementation(Ktor.androidSerialization)
                implementation(Ktor.androidCore)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(KotlinTest.junit)
                implementation(AndroidXTest.core)
                implementation(AndroidXTest.junit)
                implementation(AndroidXTest.runner)
                implementation(AndroidXTest.rules)
                implementation(Coroutines.test)
                implementation(Deps.robolectric)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Koin.koinCore)
                implementation(Ktor.ios)
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
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)

/*apply(from = rootProject.file("pom.gradle"))*/
// TODO
apply(from = rootProject.file("gradle/publish.gradle"))