package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.shared.CFlow

interface StationsAPI {
    fun getAllStations(): CFlow<StationsResultState<StationsResult>>
    fun getStationLocation(crsCode: String): StationLocation
    fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): CFlow<StationsResultState<NearestStations>>
}
