package com.intsoftdev.nrstations.shared

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoinIos(
    userDefaults: NSUserDefaults,
): KoinApplication = init(
    module {
        factory<Settings> { AppleSettings(userDefaults) }
    }
)

actual val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
}
