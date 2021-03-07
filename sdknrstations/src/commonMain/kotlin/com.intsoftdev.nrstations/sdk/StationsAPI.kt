package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.common.StationsResult

interface StationsAPI {
    suspend fun getAllStations(): StationsResultState<StationsResult>
}