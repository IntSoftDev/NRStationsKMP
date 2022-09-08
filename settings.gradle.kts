pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "NRStations"

includeBuild("convention-plugins")
include(":androidApp")
include(":sdknrstations")
