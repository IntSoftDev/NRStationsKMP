package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.StationDistances
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.provide
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class NrNearbyViewModel : NrViewModel(), StationsSdkDiComponent {

    init {
        Napier.d("init")
    }

    private var stationsSDK = this.provide<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(NrNearbyViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<NrNearbyViewState> = _uiState

    override fun onCleared() {
        Napier.d("onCleared")
        super.onCleared()
    }

    fun getNearbyStations(crsCode: String) {
        Napier.d("getNearbyStations $crsCode enter")
        viewModelScope.launch {
            val stationLocation = stationsSDK.getStationLocation(crsCode)
            stationsSDK.getNearbyStations(stationLocation.latitude, stationLocation.longitude)
                .onStart {
                    Napier.d("onStart")
                    _uiState.emit(NrNearbyViewState(isLoading = true))
                }.catch { throwable ->
                    _uiState.emit(NrNearbyViewState(error = throwable.message))
                }.collect { result ->
                    when (result) {
                        is StationsResultState.Success -> {
                            Napier.d("got stations count ${result.data.stationDistances.size}")
                            _uiState.emit(
                                NrNearbyViewState(stations = result.data)
                            )
                        }
                        is StationsResultState.Failure -> {
                            _uiState.emit(NrNearbyViewState(error = result.error?.message))
                            Napier.e("error")
                        }
                    }
                }
        }
    }

    fun getNearbyStations(latitude: Double, longitude: Double) {
        Napier.d("getNearbyStations $latitude $longitude enter")
        viewModelScope.launch {
            stationsSDK.getNearbyStations(latitude, longitude)
                .onStart {
                    Napier.d("onStart")
                    _uiState.emit(NrNearbyViewState(isLoading = true))
                }.catch { throwable ->
                    _uiState.emit(NrNearbyViewState(error = throwable.message))
                }.collect { result ->
                    when (result) {
                        is StationsResultState.Success -> {
                            Napier.d("got stations count ${result.data.stationDistances.size}")
                            _uiState.emit(
                                NrNearbyViewState(stations = result.data)
                            )
                        }
                        is StationsResultState.Failure -> {
                            _uiState.emit(NrNearbyViewState(error = result.error?.message))
                            Napier.e("error")
                        }
                    }
                }
        }
    }
}

data class NrNearbyViewState(
    val stations: StationDistances? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
