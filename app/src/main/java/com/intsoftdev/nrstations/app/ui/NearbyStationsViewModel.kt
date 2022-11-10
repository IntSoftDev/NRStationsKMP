package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class NearbyStationsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val crsCode: String = checkNotNull(savedStateHandle["stationCrsCode"])

    init {
        getStationLocation()
    }

    fun getStationLocation() {
        // TODO
    }
}