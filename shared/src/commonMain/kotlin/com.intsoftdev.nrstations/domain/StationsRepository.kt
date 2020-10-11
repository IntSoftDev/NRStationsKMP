package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This specifies the operations that need to be implemented in the data layer
 */
interface StationsRepository {
    suspend fun getAllStations(): List<StationModel>
    fun saveAllStations(version: DataVersion, stations: List<StationModel>)
    fun getModelFromCache(stationName: String?, crsCode: String?): StationModel
}