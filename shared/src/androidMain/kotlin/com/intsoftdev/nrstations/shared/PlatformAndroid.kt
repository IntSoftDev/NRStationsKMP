package com.intsoftdev.nrstations.shared

import org.koin.core.KoinApplication

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}