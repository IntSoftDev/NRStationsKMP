package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.database.StationDb
import com.intsoftdev.nrstations.database.Version

internal interface DBWrapper {
    fun insertStations(stations: List<StationLocation>)
    fun getStations(): List<StationDb>
    fun getStationLocation(stationId: String): StationDb?
    fun insertVersion(version: Version)
    fun getVersion(): Version?
    fun isEmpty(): Boolean
}
