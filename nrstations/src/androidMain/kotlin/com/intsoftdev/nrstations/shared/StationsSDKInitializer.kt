package com.intsoftdev.nrstations.shared

import android.content.Context
import com.intsoftdev.nrstations.common.APIConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun initStationsSDK(
    context: Context,
    apiConfig: APIConfig = APIConfig(),
    enableLogging: Boolean = false
) {
    appContext = context
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }

    SDKDiInitialiser.setupDi(apiConfig)
}
