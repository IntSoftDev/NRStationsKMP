rootProject.name = "StationsSDK"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    }
    versionCatalogs {
        create("isdlibs") {
            from("com.intsoftdev:isdversioncatalog:1.0.0-ALPHA-5")
        }
    }
}

include(":app")

val IMPORT_LOCAL_NRSTATIONS_KMP: String by settings

if (IMPORT_LOCAL_NRSTATIONS_KMP == "true") {
    include(":nrstations")
    includeBuild("convention-plugins")
}