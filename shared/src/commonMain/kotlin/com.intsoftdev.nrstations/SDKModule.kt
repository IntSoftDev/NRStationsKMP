package com.intsoftdev.nrstations

import org.koin.dsl.module

internal val sdkModule = module {
    single { StationsSDK() }
}