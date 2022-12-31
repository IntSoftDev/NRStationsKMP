package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This specifies the operations that need to be implemented in the data layer
 */
internal interface StationsRepository {
    fun getAllStations(): Flow<StationsResultState<StationsResult>>
    suspend fun getStationLocation(vararg crsCodes: String): List<StationLocation>
    fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): Flow<StationsResultState<NearestStations>>
}
