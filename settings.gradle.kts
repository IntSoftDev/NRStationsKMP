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

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
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
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            content {
                includeGroupAndSubgroups("com.intsoftdev")
            }
        }
    }
    versionCatalogs {
        create("isdlibs") {
            from("com.intsoftdev:isddependencies:1.0.0-ALPHA-18-SNAPSHOT")
        }
    }
}

include(":app")

val importLocalStationsKmp: String by settings

if (importLocalStationsKmp == "true") {
    include(":nrstations")
}