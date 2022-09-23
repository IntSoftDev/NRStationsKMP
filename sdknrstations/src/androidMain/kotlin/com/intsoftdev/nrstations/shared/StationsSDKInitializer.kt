package com.intsoftdev.nrstations.shared

import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
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

    initStationsSDKAndroid(
        appDeclaration = { androidContext(androidContext = context) },
        koinApp = koinApp
    )
}
