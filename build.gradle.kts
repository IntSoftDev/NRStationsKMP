
// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(isdlibs.plugins.gradleVersions)
    alias(isdlibs.plugins.ktlint) apply false

    //trick: for the same plugin versions in all sub-modules
    kotlin("multiplatform") version isdlibs.versions.kotlin.get() apply false
    kotlin("plugin.serialization") version isdlibs.versions.kotlin.get() apply false
    id("com.squareup.sqldelight") version isdlibs.versions.sqlDelight.get() apply false
    id("com.android.application") version isdlibs.versions.androidGradle.get() apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        enableExperimentalRules.set(true)
        verbose.set(true)
        filter {
            exclude { it.file.path.contains("build/") }
        }
    }

    afterEvaluate {
        tasks.named("check").configure {
            dependsOn(tasks.getByName("ktlintCheck"))
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
