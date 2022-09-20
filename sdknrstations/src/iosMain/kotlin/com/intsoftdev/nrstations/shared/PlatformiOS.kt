package com.intsoftdev.nrstations.shared

import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal actual fun getApplicationFilesDirectoryPath(): String =
    NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)[0] as String

internal actual val nrStationsPlatformModule = module {
    factory<CoroutineDispatcher>(named("NRStationsCoroutineDispatcher")) { Dispatchers.Main }

    // TODO not sure if needed
    single { Darwin.create() }

    single { StationsCallbackViewModel(get()) }
}

@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {
    fun getStationsViewModel() = getKoin().get<StationsCallbackViewModel>()
}
