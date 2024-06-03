/*
 * Copyright (C) Integrated Software Development Ltd.
 *
 * Licensed under EUPL-1.2 (see the LICENSE file for the full license governing this code).
 */

package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.domain.GetStationsUseCase
import com.intsoftdev.nrstations.shared.CFlow
import com.intsoftdev.nrstations.shared.wrap
import org.koin.core.component.get

class NrStationsSDK : StationsAPI, StationsSdkDiComponent {
    private val getStationsUseCase by lazy { get<GetStationsUseCase>() }

    override fun getAllStations(cachePolicy: CachePolicy): CFlow<StationsResultState<StationsResult>> =
        getStationsUseCase.getAllStations(cachePolicy).wrap()

    override suspend fun getStationLocation(vararg crsCodes: String?): CFlow<StationsResultState<List<StationLocation>>> =
        getStationsUseCase.getStationLocation(*crsCodes).wrap()

    override fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): CFlow<StationsResultState<NearestStations>> = getStationsUseCase.getNearbyStations(latitude, longitude).wrap()
}
