package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.database.NRStationsDb
import kotlin.time.Clock
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val stationsCacheModule =
    module {
        factory<StationsCache> {
            StationsCacheImpl(dbWrapper = get(), settings = get(), clock = get())
        }

        factory {
            NRStationsDb(get(named("StationsSqlDriver")))
        }

        factory<DBWrapper> { DBWrapperImpl(stationsDb = get()) }

        factory<Clock> { Clock.System }
    }
