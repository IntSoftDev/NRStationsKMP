package com.intsoftdev.nrstations.shared

import co.touchlab.kermit.Kermit
import co.touchlab.kermit.NSLogLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

internal actual class Platform actual constructor() {
    actual val platform: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

internal actual fun getApplicationFilesDirectoryPath(): String =
    NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)[0] as String

internal actual val nrStationsPlatformModule = module {
    factory<CoroutineDispatcher>(named("NRStationsCoroutineDispatcher")) { Dispatchers.Main }
    single { Kermit(NSLogLogger()) }
}