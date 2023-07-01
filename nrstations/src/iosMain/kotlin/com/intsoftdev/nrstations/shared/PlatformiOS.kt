package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.database.NRStationsDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val stationsPlatformModule = module {
    factory<CoroutineDispatcher>(named("NRStationsCoroutineDispatcher")) { Dispatchers.Main }

    single(named("StationsHttpEngine")) { Darwin.create() }

    single<SqlDriver>(named("StationsSqlDriver")) { NativeSqliteDriver(NRStationsDb.Schema, "NRStationsDb") }
}