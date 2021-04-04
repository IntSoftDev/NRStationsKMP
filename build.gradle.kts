buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath(Deps.kotlin_gradle_plugin)
        classpath(Deps.android_gradle_plugin)
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven(url = "https://dl.bintray.com/aakira/maven")
        maven(url = "https://kotlin.bintray.com/kotlinx/") // soon will be just jcenter()
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

