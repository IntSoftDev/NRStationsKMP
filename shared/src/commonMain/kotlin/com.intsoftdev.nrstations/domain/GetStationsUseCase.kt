package com.intsoftdev.nrstations.domain

import com.github.aakira.napier.Napier

class GetStationsUseCase(private val stationsRepository: StationsRepository) {

    suspend fun getAllStations() {
        Napier.d("getAllStations")
        stationsRepository.getAllStations()
    }
}