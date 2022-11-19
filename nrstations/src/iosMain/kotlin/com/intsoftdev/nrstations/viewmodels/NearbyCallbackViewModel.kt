package com.intsoftdev.nrstations.viewmodels

import io.github.aakira.napier.Napier

@Suppress("Unused") // Members are called from Swift
class NearbyCallbackViewModel : CallbackSdkViewModel() {

    init {
        Napier.d("init")
    }

    override val viewModel = NreNearbyViewModel()

    val stations = viewModel.uiState.asCallbacks()

    fun getNearbyStations(latitude: Double, longitude: Double, crsCode: String?) {
        if (crsCode != null) {
            Napier.d("getNearbyStations $crsCode")
            viewModel.getNearbyStations(crsCode)
        } else {
            Napier.d("getNearbyStations $latitude $longitude")
            viewModel.getNearbyStations(latitude, longitude)
        }
    }
}
