package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.maps.model.LatLng
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.viewmodels.NreNearbyViewModel

internal class NearbyStationsViewModel(
    savedStateHandle: SavedStateHandle
) : NreNearbyViewModel(), StationsSdkDiComponent {

    private val crsCode: String? = savedStateHandle["stationCrsCode"]

    fun getNearbyStations() {
        // hack until there's a proper way to pass in the user location to get their nearby stations
        if (crsCode == null) {
            this.getNearbyStations(LONDON_WATERLOO.latitude, LONDON_WATERLOO.longitude)
        } else {
            this.getNearbyStations(crsCode)
        }
    }

    companion object {
        private val LONDON_WATERLOO = LatLng(51.50329, -0.113108)
    }
}
