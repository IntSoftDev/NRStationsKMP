package com.intsoftdev.nrstations.app

import android.app.Application
import com.google.maps.android.ktx.BuildConfig
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
