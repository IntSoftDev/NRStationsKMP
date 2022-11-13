package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.SavedStateHandle
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.viewmodels.NreNearbyViewModel

internal class NearbyStationsViewModel(
    savedStateHandle: SavedStateHandle,
) : NreNearbyViewModel(), StationsSdkDiComponent {

    private val crsCode: String = checkNotNull(savedStateHandle["stationCrsCode"])

    fun getNearbyStations() {
        this.getNearbyStations(crsCode)
    }
}
