package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.StationDistances
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NreStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.provide
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class NreNearbyViewModel : NreViewModel(), StationsSdkDiComponent {
    private var stationsSDK = this.provide<NreStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(NreNearbyViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<NreNearbyViewState> = _uiState

    override fun onCleared() {
        Napier.d("onCleared")
    }

    fun getNearbyStations(crsCode: String) {
        Napier.d("getNearbyStations enter")
        viewModelScope.launch {
            val stationLocation = stationsSDK.getStationLocation(crsCode)
            stationsSDK.getNearbyStations(stationLocation.latitude, stationLocation.longitude)
                .onStart {
                    _uiState.emit(NreNearbyViewState(isLoading = true))
                }.catch { throwable ->
                    _uiState.emit(NreNearbyViewState(error = throwable.message))
                }.collect { result ->
                    when (result) {
                        is StationsResultState.Success -> {
                            Napier.d("got stations count ${result.data.size}")
                            _uiState.emit(
                                NreNearbyViewState(stations = result.data)
                            )
                        }
                        is StationsResultState.Failure -> {
                            _uiState.emit(NreNearbyViewState(error = result.error?.message))
                            Napier.e("error")
                        }
                    }
                }
        }
    }
}

data class NreNearbyViewState(
    val stations: StationDistances? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)