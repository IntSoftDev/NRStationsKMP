plugins {
    alias(isdlibs.plugins.gradleVersions)
    alias(isdlibs.plugins.kotlin.android) apply false
    alias(isdlibs.plugins.ktlint) apply false
    alias(isdlibs.plugins.ksp) apply false
    alias(isdlibs.plugins.kmpNativeCoroutines) apply false
    alias(isdlibs.plugins.kotlinMultiplatform) apply false
    alias(isdlibs.plugins.android.kotlin.multiplatform.library) apply false
    alias(isdlibs.plugins.kotlin.serialization) apply false
    alias(isdlibs.plugins.sqlDelight) apply false
    alias(isdlibs.plugins.androidApplication) apply false
    alias(isdlibs.plugins.cocoapods) apply false
    alias(isdlibs.plugins.secrets.gradle) apply false
}

subprojects {
    apply(plugin = rootProject.isdlibs.plugins.ktlint.get().pluginId)
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
    delete(rootProject.layout.buildDirectory)
}