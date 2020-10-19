package com.intsoftdev.nrstations.cache

import org.koin.dsl.module

internal val cacheModule = module {
    factory {
        StationsCache(get())
    }
}