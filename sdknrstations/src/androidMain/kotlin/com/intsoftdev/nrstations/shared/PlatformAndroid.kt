package com.intsoftdev.nrstations.shared

import android.app.Application
import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal lateinit var appContext: Context

internal actual fun getApplicationFilesDirectoryPath(): String = appContext.filesDir.absolutePath

internal actual val nrStationsPlatformModule = module {
    factory<CoroutineDispatcher>(named("NRStationsCoroutineDispatcher")) { Dispatchers.Default }

    factory<Settings> {
        AndroidSettings(
            appContext.getSharedPreferences(
                "NRSTATIONS_SETTINGS",
                Application.MODE_PRIVATE
            )
        )
    }
}
