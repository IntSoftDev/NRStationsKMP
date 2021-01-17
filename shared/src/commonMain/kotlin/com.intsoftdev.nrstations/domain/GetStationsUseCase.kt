package com.intsoftdev.nrstations.domain

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.shared.ResultState

class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    suspend fun getAllStations() : ResultState<StationsResult> {
        Napier.d("getAllStations")
        return stationsRepository.getAllStations()
    }
}