package com.intsoftdev.nrstations.app

import android.app.Application
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.app.di.viewModelModule
import com.intsoftdev.nrstations.shared.appContext
import com.intsoftdev.nrstations.shared.initDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Napier.base(DebugAntilog())

        startKoin {
            // declare used Android context
            androidContext(this@NRStationsApplication)
            modules(listOf(viewModelModule))
        }

        // if calling startKoin within app then use line below
        initDi()

        // if using another DI or not using Koin in app then call this so the library can initialise DI
        /*  initKoin {
              androidContext(this@NRStationsApplication)
          }*/
    }
}