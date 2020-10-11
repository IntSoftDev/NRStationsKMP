package com.intsoftdev.nrstations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeViewModel(private val stationsClient: StationsClient) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)

    fun getStationsFromNetwork() {
        scope.launch {
            stationsClient.getAllStations()
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
