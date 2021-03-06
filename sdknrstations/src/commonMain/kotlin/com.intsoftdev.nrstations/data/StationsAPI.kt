package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel

internal interface StationsAPI {
    suspend fun getAllStations() : List<StationModel>
    suspend fun getDataVersion() : List<DataVersion>
}