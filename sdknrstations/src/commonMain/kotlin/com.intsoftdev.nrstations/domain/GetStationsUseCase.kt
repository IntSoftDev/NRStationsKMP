package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import kotlinx.coroutines.flow.Flow

internal class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    fun getAllStations(): Flow<StationsResultState<StationsResult>> {
        return stationsRepository.getAllStations()
    }
}