package com.intsoftdev.nrstations.di

import com.intsoftdev.nrstations.cache.stationsCacheModule
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.common.toKoinProperties
import com.intsoftdev.nrstations.sdk.SdkDiContext
import com.intsoftdev.nrstations.sdk.stationsSdkModule
import com.intsoftdev.nrstations.shared.stationsPlatformModule
import kotlin.native.concurrent.ThreadLocal
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import stationsDataModule
import stationsDomainModule

private val stationsDIModules =
    module {
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
        clientModules: List<Module> = emptyList()
    ): SdkDiContext {
        StationsSdkKoinHolder.koinApplication =
            koinApplication {
                properties(apiConfig.toKoinProperties())
                modules(stationsDIModules.plus(clientModules))
            }
        return StationsSdkKoinHolder.koinApplication
    }
}

internal const val KOIN_HTTP_CLIENT = "PlatformHttpClient"
