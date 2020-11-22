package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.NativeViewModel
import com.intsoftdev.nrstations.StationsClient
import com.intsoftdev.nrstations.db.NRStationsKMPDb
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
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
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    module {
        factory<Settings> { AppleSettings(userDefaults) }
        factory { NativeViewModel(get()) }
        factory { doOnStartup }
        factory { StationsClient() }
    }
)

actual val platformModule = module {
    single<SqlDriver> { NativeSqliteDriver(NRStationsKMPDb.Schema, "NRStationsKMPDb") }

    single {
        NRStationsKMPDb(get())
    }

    factory<CoroutineDispatcher> { Dispatchers.Main }
}

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier, null)
}
