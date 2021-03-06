package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.entities.StationsEntity
import com.intsoftdev.nrstations.cache.entities.VersionEntity

internal interface DBWrapper {
    fun insertStations(stations: StationsEntity)
    fun getStations(): StationsEntity?

    fun insertVersion(version: VersionEntity)
    fun getVersion(): VersionEntity?

    fun isEmpty(): Boolean
}