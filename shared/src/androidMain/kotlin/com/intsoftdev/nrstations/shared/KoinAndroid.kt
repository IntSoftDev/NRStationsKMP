package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.db.NRStationsKMPDb
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            NRStationsKMPDb.Schema,
            get(),
            "NRStationsKMPDb"
        )
    }

    single<NRStationsKMPDb> {
        NRStationsKMPDb(get())
    }

    factory<CoroutineDispatcher> { Dispatchers.Default }

    factory<Settings> {
        AndroidSettings(get())
    }
}