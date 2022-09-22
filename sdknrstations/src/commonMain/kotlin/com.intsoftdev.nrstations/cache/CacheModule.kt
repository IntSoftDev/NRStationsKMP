package com.intsoftdev.nrstations.cache

import kotlinx.datetime.Clock
import org.koin.dsl.module

internal val stationsCacheModule = module(createdAtStart=true) {
    factory<StationsCache> {
        StationsCacheImpl(dbWrapper = get(), settings = get(), clock = get())
    }

    // single<Configuration> { RealmConfiguration.create(schema = setOf(StationDb::class)) }

    // single{ Realm.open(get()) }

    factory<DBWrapper> { DBWrapperImpl() }

    factory<Clock> { Clock.System }
}
