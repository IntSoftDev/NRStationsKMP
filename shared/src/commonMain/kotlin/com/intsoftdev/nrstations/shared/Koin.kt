package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.cache.cacheModule
import com.intsoftdev.nrstations.di.sdkModule
import dataModule
import domainModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            sdkModule,
            platformModule,
            dataModule,
            domainModule,
            cacheModule
        )
    }

fun initDi(enableNetworkLogs: Boolean = false) {
    loadKoinModules(
        listOf(
            sdkModule,
            platformModule,
            dataModule,
            domainModule,
            cacheModule
        )
    )
}

// called by iOS
fun init(iOSModule: Module) = startKoin {
    modules(
        sdkModule,
        iOSModule,
        platformModule,
        dataModule,
        domainModule,
        cacheModule
    )
}

expect val platformModule: Module