package com.intsoftdev.nrstations.di

import com.intsoftdev.nrstations.cache.stationsCacheModule // ktlint-disable import-ordering
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.common.toKoinProperties
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import com.intsoftdev.nrstations.shared.stationsPlatformModule
import kotlin.native.concurrent.ThreadLocal
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
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
internal object StationsSdkKoinHolder {
    var koinApplication = koinApplication { }
}

internal object SDKDiInitialiser {
    internal fun setupDi(
        apiConfig: APIConfig = APIConfig(),
        koinApplication: KoinApplication? = null,
        clientModules: List<Module> = emptyList()
    ) {
        if (koinApplication == null) {
            StationsSdkKoinHolder.koinApplication = koinApplication {
                properties(apiConfig.toKoinProperties())
                modules(stationsDIModules.plus(clientModules))
            }
        } else {
            koinApplication.koin.loadModules(stationsDIModules.plus(clientModules))
            apiConfig.toKoinProperties().forEach {
                koinApplication.koin.setProperty(it.key, it.value)
            }
            StationsSdkKoinHolder.koinApplication = koinApplication
        }
    }
}
