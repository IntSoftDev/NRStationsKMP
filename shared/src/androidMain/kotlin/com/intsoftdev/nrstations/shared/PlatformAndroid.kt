package com.intsoftdev.nrstations.shared

import android.content.Context

lateinit var appContext: Context

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getApplicationFilesDirectoryPath(): String = appContext.filesDir.absolutePath