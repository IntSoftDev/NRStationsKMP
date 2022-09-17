package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.database.Station
import com.intsoftdev.nrstations.database.Version

internal interface DBWrapper {
    fun insertStation(stations: Station)
    fun insertStations(stations: List<StationLocation>)
    fun getStations(): List<Station>
    fun getStationLocation(stationId: String): Station?
    fun insertVersion(version: Version)
    fun getVersion(): Version?
    fun isEmpty(): Boolean
}