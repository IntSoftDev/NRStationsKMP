package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel

interface StationsServiceAPI {
    suspend fun getAllStations() : List<StationModel>
    suspend fun getDataVersion() : List<DataVersion>
}