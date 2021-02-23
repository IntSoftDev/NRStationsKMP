package com.intsoftdev.nrstations.sdk

import org.koin.dsl.module

internal val sdkModule = module {
    single { NREStationsSDK() }
}