package com.intsoftdev.nrstations.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.StationsClient
import com.intsoftdev.nrstations.app.ui.StationsViewModel
import com.intsoftdev.nrstations.shared.appContext
import com.intsoftdev.nrstations.shared.initKoin
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Napier.base(DebugAntilog())
        initKoin(
            module {
                single<Context> { this@NRStationsApplication }
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences("NRSTATIONS_SETTINGS", MODE_PRIVATE)
                }
                viewModel { StationsViewModel(get()) }
                single { StationsClient() }
                factory {
                    { Log.i("Startup", "kotlin common call to Android") }
                }
            }
        )
    }
}