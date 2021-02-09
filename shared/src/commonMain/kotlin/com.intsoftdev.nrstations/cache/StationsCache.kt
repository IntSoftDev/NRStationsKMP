package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsVersion

interface StationsCache {
    fun insertAll(stations: List<StationModel>)
    fun insertVersion(version: DataVersion)
    fun getAllStations(): List<StationModel>?
    fun isCacheEmpty() : Boolean
    fun getVersion(): StationsVersion?
    fun getUpdateAction(): DataUpdateAction
}