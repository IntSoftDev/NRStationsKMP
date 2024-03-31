package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.di.StationsSdkKoinHolder
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Use this interface to get access to [NrStationsSDK]
 * Call platform specific [com.intsoftdev.nrstations.shared.initStationsSDK] to setup the SDK DI
 * then [org.koin.core.component.inject] & [org.koin.core.component.get] will target [StationsSdkKoinHolder]
 */
interface StationsSdkDiComponent : KoinComponent {
    override fun getKoin() = StationsSdkKoinHolder.koinApplication.koin
}

/**
 * Koin API wrapper to get the required component
 * Call [injectStations] to get the injected object from Koin
 */
inline fun <reified T> StationsSdkDiComponent.injectStations(): T = get()

typealias SdkDiContext = KoinApplication
