buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.kotlin_gradle_plugin)
        classpath(Deps.android_gradle_plugin)
    }
}
group = "com.intsoftdev.nrstations"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
