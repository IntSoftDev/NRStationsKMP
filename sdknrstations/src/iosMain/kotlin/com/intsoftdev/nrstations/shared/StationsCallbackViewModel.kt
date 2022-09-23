package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.sdk.NREStationsSDK
import io.github.aakira.napier.Napier

@Suppress("Unused") // Members are called from Swift
class StationsCallbackViewModel(
    private val stationsSDK: NREStationsSDK
) : CallbackSdkViewModel() {

    override val viewModel = StationsViewModel(stationsSDK)

    val stations = viewModel.uiState.asCallbacks()

    fun refreshStations() {
        Napier.d("refreshStations enter")
        viewModel.getAllStations()
    }
}
