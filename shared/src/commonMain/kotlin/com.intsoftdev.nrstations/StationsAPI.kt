package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.shared.ResultState

interface StationsAPI {
    suspend fun getAllStations(): ResultState<StationsResult>
}