package com.intsoftdev.nrstations.shared

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication

fun initStationsSDK(
    context: Context,
    koinApp: KoinApplication? = null,
    enableLogging: Boolean = false,
) {
    appContext = context

    initStationsSDKAndroid(
        appDeclaration = { androidContext(androidContext = context) },
        koinApp = koinApp
    )
}