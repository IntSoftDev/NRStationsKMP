package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.stationsCacheModule
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import stationsDataModule
import stationsDomainModule

val stationsModule = module {
    includes(stationsCacheModule, stationsDomainModule, stationsDataModule, stationsSdkModule)
}

internal fun initStationsSDKAndroid(
    appDeclaration: KoinAppDeclaration = {},
    koinApp: KoinApplication? = null
) {
    if (koinApp == null) {
        startKoin {
            appDeclaration()
            modules(
                stationsModule + nrStationsPlatformModule
            )
        }
    } else {
        loadKoinModules(
            stationsModule + nrStationsPlatformModule
        )
    }
}

internal fun initSDKiOS(iOSModule: Module, koinApp: KoinApplication? = null): KoinApplication {
    return if (koinApp == null) {
        startKoin {
            modules(
                stationsModule + iOSModule + nrStationsPlatformModule
            )
        }
    } else {
        loadKoinModules(
            stationsModule + iOSModule + nrStationsPlatformModule
        )
        koinApp
    }
}
