package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class StationsClient() : KoinComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    suspend fun getAllStations() {
        return getStationsUseCase.getAllStations()
    }
}