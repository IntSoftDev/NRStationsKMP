package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationDistances
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.domain.GetStationsUseCase
import com.intsoftdev.nrstations.shared.CFlow
import com.intsoftdev.nrstations.shared.wrap
import org.koin.core.component.get

class NrStationsSDK : StationsAPI, StationsSdkDiComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    override fun getAllStations(): CFlow<StationsResultState<StationsResult>> =
        getStationsUseCase.getAllStations().wrap()

    override fun getStationLocation(crsCode: String): StationLocation =
        getStationsUseCase.getStationLocation(crsCode)

    override fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): CFlow<StationsResultState<StationDistances>> =
        getStationsUseCase.getNearbyStations(latitude, longitude).wrap()
}
