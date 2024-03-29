package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
import com.intsoftdev.nrstations.viewmodels.NearbyCallbackViewModel
import com.intsoftdev.nrstations.viewmodels.StationsCallbackViewModel
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

    factory { StationsCallbackViewModel() }

    factory { NearbyCallbackViewModel() }

    single<SqlDriver>(named("StationsSqlDriver")) { NativeSqliteDriver(NRStationsDb.Schema, "NRStationsDb") }
}

@Suppress("unused") // Called from Swift
object KotlinDependencies : StationsSdkDiComponent {
    fun getStationsViewModel() = this.injectStations<StationsCallbackViewModel>()
    fun getNearbyViewModel() = this.injectStations<NearbyCallbackViewModel>()
}
