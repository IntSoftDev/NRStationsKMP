package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.model.StationsVersion

internal interface DBWrapper {
    fun insertStations(stations: StationsList)
    fun getStations(): StationsList?

    fun insertVersion(version: StationsVersion)
    fun getVersion(): StationsVersion?

    fun isEmpty(): Boolean
}