package com.intsoftdev.nrstations.shared

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.cacheModule
import dataModule
import domainModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(appModule: Module): KoinApplication {
    Napier.d("initKoin")
    val koinApplication = startKoin {
        modules(
            appModule,
            platformModule,
            dataModule,
            domainModule,
            cacheModule
        )
    }

    val koin = koinApplication.koin
    val doOnStartup =
        koin.get<() -> Unit>() // doOnStartup is a lambda which is implemented in Swift on iOS side
    doOnStartup.invoke()

    return koinApplication
}

expect val platformModule: Module