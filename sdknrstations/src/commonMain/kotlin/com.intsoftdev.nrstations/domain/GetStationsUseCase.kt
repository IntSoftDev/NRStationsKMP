package com.intsoftdev.nrstations.domain

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState

internal class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    suspend fun getAllStations() : StationsResultState<StationsResult> {
        Napier.d("getAllStations")
        return stationsRepository.getAllStations()
    }
}