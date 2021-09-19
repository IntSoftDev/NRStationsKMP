package com.intsoftdev.nrstations.domain

import io.github.aakira.napier.Napier
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.shared.CFlow
import kotlinx.coroutines.flow.Flow

internal class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    fun getAllStations(): Flow<StationsResultState<StationsResult>> {
        Napier.d("getAllStations")
        return stationsRepository.getAllStations()
    }

    fun getStationLocation(crsCode: String?) = stationsRepository.getStationLocation(crsCode)
}