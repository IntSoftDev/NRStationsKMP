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
            from("com.intsoftdev:isdversioncatalog:3.1.0-SNAPSHOT")
        }
    }
}

include(":app", ":sdknrstations")
rootProject.name = "StationsSDK"

includeBuild("convention-plugins")
