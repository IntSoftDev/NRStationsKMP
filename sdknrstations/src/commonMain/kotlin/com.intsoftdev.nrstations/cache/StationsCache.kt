package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.UpdateVersion

internal interface StationsCache {
    fun insertStations(stations: List<StationLocation>)
    fun insertVersion(version: UpdateVersion)
    fun getAllStations(): List<StationLocation>
    fun getStationLocation(crsCode: String): StationLocation
    fun getCacheState(serverVersion: Double? = null): CacheState
    fun getVersion(): UpdateVersion
}