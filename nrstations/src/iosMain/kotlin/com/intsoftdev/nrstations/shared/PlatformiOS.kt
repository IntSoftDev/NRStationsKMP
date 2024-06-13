package com.intsoftdev.nrstations.shared

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.intsoftdev.nrstations.database.NRStationsDb
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val stationsPlatformModule =
    module {
        single<SqlDriver>(named("StationsSqlDriver")) {
            NativeSqliteDriver(
                NRStationsDb.Schema,
                "NRStationsDb"
            )
        }
    }
