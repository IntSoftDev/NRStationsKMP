package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.UpdateVersion

internal interface DBWrapper {
    suspend fun insertStations(stations: List<StationLocation>)
    fun getStations(): List<StationLocation>
    fun getStationLocation(stationId: String): StationLocation?
    fun insertVersion(version: UpdateVersion)
    fun getVersion(): UpdateVersion?
    fun isEmpty(): Boolean
}
