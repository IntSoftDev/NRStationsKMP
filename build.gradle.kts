buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.kotlin_gradle_plugin)
        classpath(Deps.android_gradle_plugin)
        classpath(Deps.kotlin_serialization)
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

