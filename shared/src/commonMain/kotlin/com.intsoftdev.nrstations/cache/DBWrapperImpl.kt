package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.model.StationsVersion
import org.kodein.db.DB
import org.kodein.db.find
import org.kodein.db.useModels

internal class DBWrapperImpl(private val db: DB) : DBWrapper{
    override fun insertStations(stations: StationsList) {
        db.put(stations)
    }

    override fun getStations(): StationsList? {
        return  db.find<StationsList>().all().useModels { it.firstOrNull() }
    }

    override fun insertVersion(version: StationsVersion) {
        db.put(version)
    }

    override fun getVersion(): StationsVersion? {
        return db.find<StationsVersion>().all().useModels { it.firstOrNull() }
    }

    override fun isEmpty(): Boolean {
        return db.find<StationsList>().all().useModels { it.firstOrNull() } == null
    }
}