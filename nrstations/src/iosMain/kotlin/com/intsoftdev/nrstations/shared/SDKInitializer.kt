package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.common.APIConfig
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.KoinApplication
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initStationsSDK(
    apiConfig: APIConfig = APIConfig(),
    userDefaults: NSUserDefaults,
    koinApp: KoinApplication? = null,
    enableLogging: Boolean = false
): KoinApplication {
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    return initSDKiOS(
        apiConfig,
        module {
            factory<Settings> { NSUserDefaultsSettings(userDefaults) }
        },
        koinApp
    )
}
