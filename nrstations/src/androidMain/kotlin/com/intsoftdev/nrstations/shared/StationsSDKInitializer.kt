package com.intsoftdev.nrstations.shared

import android.content.Context
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.di.SDKDiInitialiser
import com.intsoftdev.nrstations.sdk.SdkDiContext
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun initStationsSDK(
    context: Context,
    apiConfig: APIConfig = APIConfig(),
    sdkDiContext: SdkDiContext? = null,
    enableLogging: Boolean = false
) {
    appContext = context
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }

    SDKDiInitialiser.setupDi(apiConfig, sdkDiContext)
}
