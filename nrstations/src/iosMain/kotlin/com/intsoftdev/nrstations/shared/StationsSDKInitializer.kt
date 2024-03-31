/*
 * Copyright (C) Integrated Software Development Ltd.
 *
 * Licensed under EUPL-1.2 (see the LICENSE file for the full license governing this code).
 */

package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.di.SDKDiInitialiser
import com.intsoftdev.nrstations.di.StationsSdkKoinHolder
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * Top level iOS entry point into the SDK
 *
 * @param apiConfig optional instance of [APIConfig]
 * Swift can use [com.intsoftdev.nrstations.common.DefaultAPIConfig]
 * @param userDefaults instance of iOS NSUserDefaults
 * @param enableLogging default is false
 */
@Suppress("unused")
fun initStationsSDK(
    apiConfig: APIConfig = APIConfig(),
    userDefaults: NSUserDefaults,
    enableLogging: Boolean = false
): KoinApplication {
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    return initSDKiOS(
        apiConfig,
        module {
            factory<Settings> { NSUserDefaultsSettings(userDefaults) }
        }
    )
}

internal fun initSDKiOS(
    apiConfig: APIConfig,
    iOSModule: Module
): KoinApplication {
    SDKDiInitialiser.setupDi(apiConfig, listOf(iOSModule))
    return StationsSdkKoinHolder.koinApplication
}
