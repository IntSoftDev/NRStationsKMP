package com.intsoftdev.nrstations.shared

import android.app.Application
import android.content.Context
import com.intsoftdev.nrstations.database.NRStationsDb
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal lateinit var appContext: Context

internal actual val stationsPlatformModule = module {

    factory<CoroutineDispatcher>(named("NRStationsCoroutineDispatcher")) { Dispatchers.Default }

    factory<Settings> {
        AndroidSettings(appContext.getSharedPreferences("NRSTATIONS_SETTINGS", Application.MODE_PRIVATE))
    }

    single<SqlDriver>(named("StationsSqlDriver")) {
        AndroidSqliteDriver(
            NRStationsDb.Schema,
            get(),
            "NRStationsDb"
        )
    }
}
