package com.intsoftdev.nrstations.shared

import android.app.Application
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.intsoftdev.nrstations.database.NRStationsDb
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal lateinit var appContext: Context

internal actual val stationsPlatformModule =
    module {

        factory<Settings> {
            SharedPreferencesSettings(
                appContext.getSharedPreferences(
                    "NRSTATIONS_SETTINGS",
                    Application.MODE_PRIVATE
                )
            )
        }

        single<SqlDriver>(named("StationsSqlDriver")) {
            AndroidSqliteDriver(
                schema = NRStationsDb.Schema,
                context = appContext,
                name = "NRStationsDb"
            )
        }
    }
