package com.intsoftdev.nrstations.shared

import android.app.Application
import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {

    factory<CoroutineDispatcher> { Dispatchers.Default }

    factory<Settings> {
        AndroidSettings(appContext.getSharedPreferences("NRSTATIONS_SETTINGS", Application.MODE_PRIVATE))
    }
}