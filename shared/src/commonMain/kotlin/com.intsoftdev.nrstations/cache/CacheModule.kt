package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.shared.getApplicationFilesDirectoryPath
import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.impl.inDir
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import org.koin.dsl.module

internal val cacheModule = module {
    factory {
        StationsCache(get())
    }

    factory {
        DB.inDir(getApplicationFilesDirectoryPath())
            .open("stations_db", TypeTable {
                root<StationsList>()
            }, KotlinxSerializer())
    }
}