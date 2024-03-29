package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NearbyStationsViewModel : ViewModel(), StationsSdkDiComponent {

    private val stationsSDK = this.injectStations<NrStationsSDK>()

    private val _uiState = MutableStateFlow<NearbyStationsUiState>(NearbyStationsUiState.Loading)

    val uiState: StateFlow<NearbyStationsUiState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        NearbyStationsUiState.Loading
    )

    fun getNearbyStations(latitude: Double, longitude: Double) {
        Napier.d("getNearbyStations $latitude $longitude enter")
        viewModelScope.launch {
            stationsSDK.getNearbyStations(latitude, longitude)
                .onStart {
                    Napier.d("onStart")
                    _uiState.emit(NearbyStationsUiState.Loading)
                }.catch { throwable ->
                    _uiState.emit(NearbyStationsUiState.Error(error = throwable.message))
                }.collect { result ->
                    when (result) {
                        is StationsResultState.Success -> {
                            Napier.d("got stations count ${result.data.stationDistances.size}")
                            _uiState.emit(
                                NearbyStationsUiState.Loaded(stations = result.data)
                            )
                        }

                        is StationsResultState.Failure -> {
                            _uiState.emit(NearbyStationsUiState.Error(error = result.error?.message))
                            Napier.e("error")
                        }
                    }
                }
        }
    }
}

sealed interface NearbyStationsUiState {
    data object Loading : NearbyStationsUiState
    data class Error(val error: String?) : NearbyStationsUiState
    data class Loaded(
        val stations: NearestStations
    ) : NearbyStationsUiState
}
