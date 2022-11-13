package com.intsoftdev.nrstations.viewmodels

import io.github.aakira.napier.Napier

@Suppress("Unused") // Members are called from Swift
class NearbyCallbackViewModel : CallbackSdkViewModel() {

    override val viewModel = NreNearbyViewModel()

    val stations = viewModel.uiState.asCallbacks()

    fun getNearbyStations(crsCode: String) {
        Napier.d("getNearbyStations $crsCode")
        viewModel.getNearbyStations(crsCode)
    }
}