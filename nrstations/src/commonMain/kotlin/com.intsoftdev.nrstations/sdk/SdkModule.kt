package com.intsoftdev.nrstations.sdk

import org.koin.dsl.module

internal val stationsSdkModule =
    module {
        single { NrStationsSDK() }
    }
