package com.intsoftdev.nrstations.app

import android.app.Application
import com.intsoftdev.nrstations.app.di.viewModelModule
import com.intsoftdev.nrstations.shared.initStationsSDK
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val koinApp = startKoin {
            // declare used Android context
            androidContext(this@NRStationsApplication)
            modules(listOf(viewModelModule))
        }

        initStationsSDK(
            context = this@NRStationsApplication,
            koinApp = koinApp,
            enableLogging = true
        )
    }
}