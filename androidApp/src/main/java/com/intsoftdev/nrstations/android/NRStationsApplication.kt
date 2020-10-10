package com.intsoftdev.nrstations.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.initKoin
import org.koin.dsl.module

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin(
            module {
                single<Context> { this@NRStationsApplication }
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences("NRSTATIONS_SETTINGS", MODE_PRIVATE)
                }
                factory {
                    { Log.i("Startup", "kotlin common call to Android") }
                }
            }
        )
    }
}