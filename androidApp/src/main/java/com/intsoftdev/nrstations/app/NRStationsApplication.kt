package com.intsoftdev.nrstations.app

import android.app.Application
import co.touchlab.kermit.Kermit
import com.intsoftdev.nrstations.app.di.uiModule
import com.intsoftdev.nrstations.shared.initStationsSDK
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NRStationsApplication : Application() {

    private val logger: Kermit by inject()

    override fun onCreate() {
        super.onCreate()

        val koinApp = startKoin {
            // declare used Android context
            androidContext(this@NRStationsApplication)
            modules(listOf(uiModule))
        }

        initStationsSDK(
            context = this@NRStationsApplication,
            koinApp = koinApp,
            enableLogging = true
        )
        logger.d { "NRStationsApplication" }
    }
}