package com.intsoftdev.nrstations.domain

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.shared.ResultState

class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    suspend fun getAllStations() : ResultState<List<StationModel>> {
        Napier.d("getAllStations")
        return stationsRepository.getAllStations()
    }
}