package com.intsoftdev.nrstations.viewmodels

import io.github.aakira.napier.Napier

@Suppress("Unused") // Members are called from Swift
class StationsCallbackViewModel : CallbackSdkViewModel() {

    init {
        Napier.d("init")
    }

    override val viewModel = NrStationsViewModel()

    val stations = viewModel.uiState.asCallbacks()

    fun refreshStations() {
        Napier.d("refreshStations enter")
        viewModel.getAllStations()
    }
}
