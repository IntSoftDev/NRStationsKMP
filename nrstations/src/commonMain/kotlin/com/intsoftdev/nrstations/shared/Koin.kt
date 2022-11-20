package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.stationsCacheModule // ktlint-disable import-ordering
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.common.toKoinProperties
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import kotlin.native.concurrent.ThreadLocal
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.core.KoinApplication
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

@ThreadLocal
internal object SDKKoinHolder {
    var koinApplication = koinApplication { }
}

internal object SDKDiInitialiser {
    internal fun setupDi(
        apiConfig: APIConfig = APIConfig(),
        clientModules: List<Module> = emptyList()
    ) {
        SDKKoinHolder.koinApplication = koinApplication {
            properties(apiConfig.toKoinProperties())
            modules(stationsDIModules.plus(clientModules))
        }
    }
}

internal fun initSDKiOS(
    apiConfig: APIConfig,
    iOSModule: Module
): KoinApplication {
    SDKDiInitialiser.setupDi(apiConfig, listOf(iOSModule))
    return SDKKoinHolder.koinApplication
}
