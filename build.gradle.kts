buildscript {
    repositories {
        gradlePluginPortal()
        google()
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
        jcenter()
        maven(url = "https://dl.bintray.com/ekito/koin")
        maven(url = "https://dl.bintray.com/aakira/maven")
        maven(url = "https://dl.bintray.com/intsoftdev/rep1") // preceeded by mavenLocal dependency if present
        // Needed by Kodein DB until KotlinX.DateTime is published to jCenter.
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

