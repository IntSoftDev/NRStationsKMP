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
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    }
    versionCatalogs {
        create("isdlibs") {
            from("com.intsoftdev:isdversioncatalog:1.0.0-ALPHA-2")
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