package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.stationsCacheModule
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.common.toKoinProperties
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import stationsDataModule
import stationsDomainModule

private val stationsDIModules = module {
    includes(
        stationsSdkModule,
        stationsDataModule,
        stationsDomainModule,
        stationsCacheModule,
        stationsPlatformModule
    )
}

internal fun initStationsSDKAndroid(
    apiConfig: APIConfig,
    appDeclaration: KoinAppDeclaration = {},
    koinApp: KoinApplication? = null
) {
    if (koinApp == null) {
        startKoin {
            properties(apiConfig.toKoinProperties())
            appDeclaration()
            modules(
                stationsDIModules
            )
        }
    } else {
        koinApp.properties(apiConfig.toKoinProperties())
        loadKoinModules(
            stationsDIModules
        )
    }
}

internal fun initSDKiOS(
    apiConfig: APIConfig,
    iOSModule: Module,
    koinApp: KoinApplication? = null
): KoinApplication {
    return if (koinApp == null) {
        startKoin {
            properties(apiConfig.toKoinProperties())
            modules(
                stationsDIModules + iOSModule
            )
        }
    } else {
        koinApp.properties(apiConfig.toKoinProperties())
        loadKoinModules(
            stationsDIModules + iOSModule
        )
        koinApp
    }
}
