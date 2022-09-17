package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.database.NRStationsDb
import kotlinx.datetime.Clock
import org.koin.dsl.module

internal val stationsCacheModule = module {
    factory<StationsCache> {
        StationsCacheImpl(dbWrapper = get(), settings = get(), clock = get())
    }

    factory {
        NRStationsDb(get())
    }

    factory<DBWrapper> { DBWrapperImpl(stationsDb = get()) }

    factory<Clock> { Clock.System }
}
