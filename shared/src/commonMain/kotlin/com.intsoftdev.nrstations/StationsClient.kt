package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.domain.GetStationsUseCase
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.shared.ResultState
import org.koin.core.KoinComponent
import org.koin.core.get

class StationsClient() : KoinComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    suspend fun getAllStations() : ResultState<List<StationModel>> {
        return getStationsUseCase.getAllStations()
    }
}