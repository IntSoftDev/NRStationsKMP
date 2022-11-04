@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    versionCatalogs {
        create("isdlibs") {
            from("com.intsoftdev:isdversioncatalog:0.0.1")
        }
    }
}

include(":app")
rootProject.name = "StationsSDK"

val IMPORT_LOCAL_NRSTATIONS_KMP: String by settings

if (IMPORT_LOCAL_NRSTATIONS_KMP == "true") {
    include(":nrstations")
    includeBuild("convention-plugins")
}