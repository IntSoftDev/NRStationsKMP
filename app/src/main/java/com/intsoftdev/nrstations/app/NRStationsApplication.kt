package com.intsoftdev.nrstations.app

import android.app.Application
import android.util.Log
import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.shared.initStationsSDK

class NRStationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initStationsSDK(
            context = this@NRStationsApplication,
            enableLogging = BuildConfig.DEBUG
        )
    }
}
