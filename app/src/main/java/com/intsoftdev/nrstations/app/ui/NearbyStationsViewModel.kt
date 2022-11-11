package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import io.github.aakira.napier.Napier
import org.koin.core.component.inject

class NearbyStationsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), StationsSdkDiComponent {

    private val stationsSDK: NREStationsSDK by inject()

    val crsCode: String = checkNotNull(savedStateHandle["stationCrsCode"])

    init {
        getStationLocation()
    }

    fun getStationLocation() {
        val stationLocation = stationsSDK.getStationLocation(crsCode)
        Napier.d("station is $stationLocation")
    }
}