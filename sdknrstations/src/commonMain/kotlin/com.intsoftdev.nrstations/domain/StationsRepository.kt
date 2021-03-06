package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.sdk.ResultState

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This specifies the operations that need to be implemented in the data layer
 */
internal interface StationsRepository {
    suspend fun getAllStations(): ResultState<StationsResult>
    //fun saveResult(stationsResult: StationsResult)
   /* fun getAllStationsFromCache(): List<StationModel>
    fun getVersionFromCache(): StationsVersion?*/
}