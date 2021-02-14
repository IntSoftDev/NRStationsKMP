package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsVersion

internal interface StationsCache {
    fun insertStations(stations: List<StationModel>)
    fun insertVersion(version: DataVersion)
    fun getAllStations(): List<StationModel>?
    fun isCacheEmpty() : Boolean
    fun getVersion(): StationsVersion?
    fun getUpdateAction(): DataUpdateAction
}