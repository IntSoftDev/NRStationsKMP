package com.intsoftdev.nrstations.shared

import android.content.Context
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication

fun initStationsSDK(
    context: Context,
    koinApp: KoinApplication? = null,
    enableLogging: Boolean = false,
) {
    appContext = context
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }

    initSDKAndroid(
        appDeclaration = { androidContext(androidContext = context) },
        koinApp = koinApp
    )
}