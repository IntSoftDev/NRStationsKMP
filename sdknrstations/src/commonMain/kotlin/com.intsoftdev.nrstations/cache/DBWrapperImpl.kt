package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.entities.StationEntity
import com.intsoftdev.nrstations.cache.entities.StationsEntity
import com.intsoftdev.nrstations.cache.entities.VersionEntity
import org.kodein.db.DB
import org.kodein.db.find
import org.kodein.db.useModels

internal class DBWrapperImpl(private val db: DB) : DBWrapper {
    override fun insertStations(stations: StationsEntity) {
        db.put(stations)
    }

    override fun getStations(): StationsEntity? {
        return db.find<StationsEntity>().all().useModels { it.firstOrNull() }
    }

    override fun getStationLocation(stationId: String): StationEntity? {
        return getStations()?.stations?.firstOrNull { it.crsCode == stationId }
    }

    override fun insertVersion(version: VersionEntity) {
        db.put(version)
    }

    override fun getVersion(): VersionEntity? {
        return db.find<VersionEntity>().all().useModels { it.firstOrNull() }
    }

    override fun isEmpty(): Boolean {
        return db.find<StationsEntity>().all().useModels { it.firstOrNull() } == null
    }
}