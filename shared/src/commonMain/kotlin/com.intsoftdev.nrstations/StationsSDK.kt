package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.domain.GetStationsUseCase
import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.shared.ResultState
import org.koin.core.KoinComponent
import org.koin.core.get

class StationsSDK : StationsAPI, KoinComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    override suspend fun getAllStations(): ResultState<StationsResult> {
        return getStationsUseCase.getAllStations()
    }
}