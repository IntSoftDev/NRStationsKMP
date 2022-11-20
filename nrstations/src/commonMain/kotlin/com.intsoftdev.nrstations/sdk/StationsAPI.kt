package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.common.StationDistances
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.shared.CFlow
import com.intsoftdev.nrstations.shared.SDKKoinHolder
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Use this interface to get access to [NrStationsSDK]
 * Call [initStationsSDK] to setup the SDK DI
 * then [org.koin.core.component.inject] & [org.koin.core.component.get] will target [SDKKoinHolder]
 */
interface StationsSdkDiComponent : KoinComponent {
    override fun getKoin() = SDKKoinHolder.koinApplication.koin
}

/**
 * Koin API wrapper to get the required component
 * Call [provide] to get the injected object from Koin
 */
inline fun <reified T> StationsSdkDiComponent.provide(): T = get()

interface StationsAPI {
    fun getAllStations(): CFlow<StationsResultState<StationsResult>>
    fun getStationLocation(crsCode: String): StationLocation
    fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): CFlow<StationsResultState<StationDistances>>
}
