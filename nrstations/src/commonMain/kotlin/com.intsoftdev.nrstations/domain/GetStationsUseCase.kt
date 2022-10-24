package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow

internal class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    fun getAllStations(): Flow<StationsResultState<StationsResult>> {
        Napier.d("getAllStations")
        return stationsRepository.getAllStations()
    }

    fun getStationLocation(crsCode: String?) = stationsRepository.getStationLocation(crsCode)
}
