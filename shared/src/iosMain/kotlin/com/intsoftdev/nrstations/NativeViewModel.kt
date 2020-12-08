package com.intsoftdev.nrstations

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.shared.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeViewModel(private val stationsClient: StationsClient) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main)

    fun getStationsFromNetwork() {
        scope.launch {
            Napier.d("NativeViewModel")
            stationsClient.getAllStations().also {
                when(it) {
                    is ResultState.Success -> Napier.d("received stations data")
                    is ResultState.Failure -> {
                        Napier.e("error ${it.error}")
                    }
                }
            }
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
