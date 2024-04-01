/*
 * Copyright (C) Integrated Software Development Ltd.
 *
 * Licensed under EUPL-1.2 (see the LICENSE file for the full license governing this code).
 */

package com.intsoftdev.nrstations.shared

import android.content.Context
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.di.SDKDiInitialiser
import com.intsoftdev.nrstations.sdk.SdkDiContext
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Top level Android entry point into the SDK
 *
 * @param context Application context
 * @param apiConfig optional instance of [APIConfig]
 * @param enableLogging default is false
 */
fun initStationsSDK(
    context: Context,
    apiConfig: APIConfig = APIConfig(),
    enableLogging: Boolean = false
): SdkDiContext {
    appContext = context
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    return SDKDiInitialiser.setupDi(apiConfig)
}
