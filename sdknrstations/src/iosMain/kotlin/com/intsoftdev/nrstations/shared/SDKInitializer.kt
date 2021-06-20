package com.intsoftdev.nrstations.shared

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initStationsSDK(
    userDefaults: NSUserDefaults,
    enableLogging: Boolean = false
): KoinApplication {
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    return initSDKiOS(
        module {
            factory<Settings> { AppleSettings(userDefaults) }
        }
    )
}
