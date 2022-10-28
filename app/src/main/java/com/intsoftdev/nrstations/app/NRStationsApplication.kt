package com.intsoftdev.nrstations.app

import android.app.Application
import android.util.Log
import com.intsoftdev.nrstations.app.di.viewModelModule
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.shared.initStationsSDK
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val koinApp = startKoin {
            if (BuildConfig.DEBUG) {
                logger(AndroidLogger())
            }
            // declare used Android context
            androidContext(this@NRStationsApplication)
            modules(listOf(viewModelModule))
        }

        Log.d("NRStationsApplication", "enter")

        initStationsSDK(
            context = this@NRStationsApplication,
            koinApp = koinApp,
            enableLogging = BuildConfig.DEBUG,
            apiConfig = APIConfig(
                serverUrl = "https://onrails-test.azurewebsites.net/"
                // Can pass in license key here
                // licenseKey = BuildConfig.ISD_API_KEY
            )
        )
    }
}
