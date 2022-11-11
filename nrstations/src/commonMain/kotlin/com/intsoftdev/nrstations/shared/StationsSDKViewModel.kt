package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.provide
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StationsSDKViewModel: SdkViewModel(), StationsSdkDiComponent {

    private var stationsSDK = this.provide<NREStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(StationsSDKViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<StationsSDKViewState> = _uiState

    override fun onCleared() {
        Napier.d("onCleared")
    }

    fun getAllStations() {
        Napier.d("getAllStations enter")
        viewModelScope.launch {
            Napier.d("getAllStations launch")
            stationsSDK.getAllStations().onStart {
                _uiState.emit(StationsSDKViewState(isLoading = true))
            }.catch { throwable ->
                _uiState.emit(StationsSDKViewState(error = throwable.message))
            }.collect { stationsResult ->
                when (stationsResult) {
                    is StationsResultState.Success -> {
                        Napier.d("got stations count ${stationsResult.data.stations.size}")
                        _uiState.emit(
                            StationsSDKViewState(stations = stationsResult.data.stations)
                        )
                    }
                    is StationsResultState.Failure -> {
                        _uiState.emit(StationsSDKViewState(error = stationsResult.error?.message))
                        Napier.e("error")
                    }
                }
            }
        }
    }
}

data class StationsSDKViewState(
    val stations: List<StationLocation>? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
