package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.entities.StationsEntity
import com.intsoftdev.nrstations.shared.getApplicationFilesDirectoryPath
import kotlinx.datetime.Clock
import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.impl.inDir
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import org.koin.dsl.module

internal val stationsCacheModule = module(override=true) {
    factory<StationsCache> {
        StationsCacheImpl(dbWrapper = get(), settings = get(), clock = get())
    }

    factory {
        DB.inDir(getApplicationFilesDirectoryPath())
            .open("stations_db", TypeTable {
                root<StationsEntity>()
            }, KotlinxSerializer())
    }

    factory<DBWrapper> { DBWrapperImpl(db = get()) }

    factory<Clock> { Clock.System }
}