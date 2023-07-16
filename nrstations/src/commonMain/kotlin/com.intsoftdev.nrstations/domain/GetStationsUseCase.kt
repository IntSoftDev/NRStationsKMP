package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class GetStationsUseCase(
    private val stationsRepository: StationsRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    fun getAllStations(cachePolicy: CachePolicy): Flow<StationsResultState<StationsResult>> {
        Napier.d("getAllStations cachePolicy $cachePolicy")
        return stationsRepository.getAllStations(cachePolicy = cachePolicy)
    }

    suspend fun getStationLocation(vararg crsCodes: String?): Flow<StationsResultState<List<StationLocation>>> {
        return flow<StationsResultState<List<StationLocation>>> {
            val codes = crsCodes.mapNotNull { it }.toTypedArray()
            val stationLocations =
                stationsRepository.getStationLocation(*codes)
            emit(StationsResultState.Success(stationLocations))
        }.catch { throwable ->
            emit(StationsResultState.Failure(throwable))
        }.flowOn(coroutineDispatcher)
    }

    fun getNearbyStations(latitude: Double, longitude: Double) =
        stationsRepository.getNearbyStations(latitude, longitude)
}
