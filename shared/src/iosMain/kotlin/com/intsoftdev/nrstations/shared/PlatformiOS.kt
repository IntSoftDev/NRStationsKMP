package com.intsoftdev.nrstations.shared


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import platform.Foundation.*
import platform.UIKit.UIDevice

internal actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

internal actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

internal actual fun getApplicationFilesDirectoryPath(): String =
    NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)[0] as String

internal actual val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
}