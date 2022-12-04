package com.intsoftdev.nrstations.app

import android.app.Application
import android.util.Log
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.shared.initStationsSDK

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("NRStationsApplication", "enter")

        initStationsSDK(
            context = this@NRStationsApplication,
            enableLogging = BuildConfig.DEBUG,
            apiConfig = APIConfig(
                serverUrl = "https://onrails-test.azurewebsites.net"
                // Can pass in license key here
                // licenseKey = BuildConfig.ISD_API_KEY
            )
        )
    }
}
