package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.sdk.NREStationsSDK
import io.github.aakira.napier.Napier

@Suppress("Unused") // Members are called from Swift
class StationsCallbackViewModel() : CallbackSdkViewModel() {

    override val viewModel = StationsSDKViewModel()

    val stations = viewModel.uiState.asCallbacks()

    fun refreshStations() {
        Napier.d("refreshStations enter")
        viewModel.getAllStations()
    }
}
