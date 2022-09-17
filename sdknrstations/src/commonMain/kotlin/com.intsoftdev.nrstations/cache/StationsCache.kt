package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.model.station.DataVersion

internal interface StationsCache {
    fun insertStations(stations: List<StationLocation>)
    fun insertVersion(version: DataVersion)
    fun getAllStations(): List<StationLocation>
    fun getStationLocation(crsCode: String): StationLocation
    fun getCacheState(serverVersion: Double? = null): CacheState
    fun getVersion(): DataVersion
}
