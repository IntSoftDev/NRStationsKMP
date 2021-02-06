package com.intsoftdev.nrstations.shared

import android.content.Context
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

fun initStationsSDK(
    context: Context,
    enableLogging: Boolean = false,
    initializeKoin: Boolean = false
) {
    appContext = context
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    initSDKAndroid(
        appDeclaration = { androidContext(androidContext = context) },
        doInit = initializeKoin
    )
}