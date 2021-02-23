package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.cacheModule
import com.intsoftdev.nrstations.sdk.sdkModule
import dataModule
import domainModule
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

private val diModules = listOf(
    sdkModule,
    dataModule,
    domainModule,
    cacheModule
)

internal fun initSDKAndroid(
    appDeclaration: KoinAppDeclaration = {},
    koinApp: KoinApplication? = null
) {
    if (koinApp == null) {
        startKoin {
            appDeclaration()
            modules(
                diModules + platformModule
            )
        }

    } else {
        loadKoinModules(
            diModules + platformModule
        )
    }
}

internal fun initSDKiOS(iOSModule: Module) = startKoin {
    modules(
        diModules + iOSModule + platformModule
    )
}