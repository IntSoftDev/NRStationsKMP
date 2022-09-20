package com.intsoftdev.nrstations.cache

import kotlinx.datetime.Clock
import org.koin.dsl.module

internal val stationsCacheModule = module {
    factory<StationsCache> {
        StationsCacheImpl(settings = get(), clock = get())
    }

    factory<Clock> { Clock.System }
}
