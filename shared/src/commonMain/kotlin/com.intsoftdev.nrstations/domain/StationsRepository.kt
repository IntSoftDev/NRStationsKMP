package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.shared.ResultState

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This specifies the operations that need to be implemented in the data layer
 */
interface StationsRepository {
    suspend fun getAllStations(): ResultState<List<StationModel>>
    fun saveAllStations(stationsResult: StationsResult)
    fun getModelFromCache(stationName: String?, crsCode: String?): StationModel
    fun getAllStationsFromCache(): List<StationModel>
}