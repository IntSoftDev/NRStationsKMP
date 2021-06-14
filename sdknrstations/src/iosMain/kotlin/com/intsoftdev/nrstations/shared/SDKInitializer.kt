package com.intsoftdev.nrstations.shared


import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initStationsSDK(
    userDefaults: NSUserDefaults,
    enableLogging: Boolean = false
): KoinApplication {

    return initSDKiOS(
        module {
            factory<Settings> { AppleSettings(userDefaults) }
        }
    )
}
