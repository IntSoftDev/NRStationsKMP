package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class NREStationsSDK : StationsAPI, KoinComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    override suspend fun getAllStations(): StationsResultState<StationsResult> {
        return getStationsUseCase.getAllStations()
    }
}