object Versions {
    val min_sdk = 21
    val target_sdk = 30
    val compile_sdk = 30

    val kotlin = "1.4.21"
    val kotlin_gradle_plugin = "1.4.20"
    val jfrog_bintray_plugin = "1.8.4"
    val androidx_test = "1.2.0"
    val androidx_test_ext = "1.1.1"
    val android_gradle_plugin = "4.1.0"
    val buildToolsVersion = "29.0.0"
    val junit = "4.13"
    val cocoapodsext = "0.11"
    val activity = "1.1.0"
    val lifecycle = "2.2.0"
    val arch = "2.1.0"
    val ktlint_gradle_plugin = "9.2.1"
    val koin = "3.0.0-alpha-4"
    val napier = "1.5.0-alpha1"
    val ktor = "1.5.0"
    val coroutines = "1.4.2-native-mt"
    val multiplatformSettings = "0.7"
    val kodein_db = "0.5.0-beta"
    val material = "1.2.1"
    val intsoftdev_stations_client_version = "0.15"
}

object Deps {
    val app_compat_x = "androidx.appcompat:appcompat:1.2.0"
    val material_x = "com.google.android.material:material:1.2.1"
    val swipeLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"

    val core_ktx = "androidx.core:core-ktx:1.2.0"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.1"
    val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_gradle_plugin}"
    val jfrog_gradle_plugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.jfrog_bintray_plugin}"
    val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    val activity_exts = "androidx.activity:activity-ktx:${Versions.activity}"

    val junit = "junit:junit:${Versions.junit}"
    val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    val napier_logger = "com.github.aakira:napier:${Versions.napier}"
    val multiplatformSettings = "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}"
    val nrstationsClientDebug = "com.intsoftdev:nrstations-androidDebug:${Versions.intsoftdev_stations_client_version}"
}

object Ktor {
    val commonCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    val commonJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    val commonLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    val jvmCore =     "io.ktor:ktor-client-core-jvm:${Versions.ktor}"
    val androidCore = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    val jvmJson =     "io.ktor:ktor-client-json-jvm:${Versions.ktor}"
    val jvmLogging =     "io.ktor:ktor-client-logging-jvm:${Versions.ktor}"
    val ios =         "io.ktor:ktor-client-ios:${Versions.ktor}"
    val iosCore =     "io.ktor:ktor-client-core-native:${Versions.ktor}"
    val iosJson =     "io.ktor:ktor-client-json-native:${Versions.ktor}"
    val iosLogging =     "io.ktor:ktor-client-logging-native:${Versions.ktor}"
    val commonSerialization ="io.ktor:ktor-client-serialization:${Versions.ktor}"
    val androidSerialization ="io.ktor:ktor-client-serialization-jvm:${Versions.ktor}"
}

object Coroutines {
    val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object Koin {
    val koinCore = "org.koin:koin-core:${Versions.koin}"
    val koinTest = "org.koin:koin-test:${Versions.koin}"
    val android = "org.koin:koin-android:${Versions.koin}"
    val androidViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
}

object KodeinDb {
    val kodeinDb = "org.kodein.db:kodein-db:${Versions.kodein_db}"
    val kodeinSerializer = "org.kodein.db:kodein-db-serializer-kotlinx:${Versions.kodein_db}"
}

object AndroidXTest {
    val core = "androidx.test:core:${Versions.androidx_test}"
    val junit = "androidx.test.ext:junit:${Versions.androidx_test_ext}"
    val runner = "androidx.test:runner:${Versions.androidx_test}"
    val rules = "androidx.test:rules:${Versions.androidx_test}"
}

object KotlinTest {
    val common = "org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}"
    val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}"
    val jvm = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val junit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
}