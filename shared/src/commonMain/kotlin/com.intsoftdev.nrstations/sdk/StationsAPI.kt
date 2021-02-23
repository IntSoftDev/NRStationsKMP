package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationsResult

interface StationsAPI {
    suspend fun getAllStations(): ResultState<StationsResult>
}