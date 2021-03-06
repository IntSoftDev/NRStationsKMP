package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.stationsCacheModule
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import stationsDataModule
import stationsDomainModule
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

private val stationsDIModules = listOf(
    stationsSdkModule,
    stationsDataModule,
    stationsDomainModule,
    stationsCacheModule
)

internal fun initStationsSDKAndroid(
    appDeclaration: KoinAppDeclaration = {},
    koinApp: KoinApplication? = null
) {
    if (koinApp == null) {
        startKoin {
            appDeclaration()
            modules(
                stationsDIModules + nrStationsPlatformModule
            )
        }

    } else {
        loadKoinModules(
            stationsDIModules + nrStationsPlatformModule
        )
    }
}

internal fun initSDKiOS(iOSModule: Module) = startKoin {
    modules(
        stationsDIModules + iOSModule + nrStationsPlatformModule
    )
}