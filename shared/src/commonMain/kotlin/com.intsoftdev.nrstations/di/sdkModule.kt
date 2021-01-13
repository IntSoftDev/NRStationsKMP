package com.intsoftdev.nrstations.di

import com.intsoftdev.nrstations.StationsClient
import org.koin.dsl.module

val sdkModule = module {
    single { StationsClient() }
}