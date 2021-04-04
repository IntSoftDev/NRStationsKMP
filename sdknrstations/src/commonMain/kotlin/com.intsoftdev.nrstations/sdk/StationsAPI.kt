package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.shared.CFlow
import kotlinx.coroutines.flow.Flow

interface StationsAPI {
    fun getAllStations(): CFlow<StationsResultState<StationsResult>>
}