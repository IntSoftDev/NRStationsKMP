package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StationsViewModel(
    private var stationsSDK: NREStationsSDK
) : SdkViewModel() {

    // Backing property to avoid state updates from other classes
    // replaced MutableStateFlow as it doesn't re-emit same value
    private val _uiState = MutableSharedFlow<StationsViewState>()

    // The UI collects from this StateFlow to get its state updates
    val uiState: SharedFlow<StationsViewState> = _uiState

    override fun onCleared() {
        Napier.d("onCleared")
    }

    fun getAllStations() {
        Napier.d("getAllStations enter")
        viewModelScope.launch {
            Napier.d("getAllStations launch")
            stationsSDK.getAllStations().onStart {
                _uiState.emit(StationsViewState(isLoading = true))
            }.catch { throwable ->
                _uiState.emit(StationsViewState(error = throwable.message))
            }.collect { stationsResult ->
                when (stationsResult) {
                    is StationsResultState.Success -> {
                        Napier.d("got stations count ${stationsResult.data.stations.size}")
                        _uiState.emit(
                            StationsViewState(stations = stationsResult.data.stations)
                        )
                    }
                    is StationsResultState.Failure -> {
                        _uiState.emit(StationsViewState(error = stationsResult.error?.message))
                        Napier.e("error")
                    }
                }
            }
        }
    }
}

data class StationsViewState(
    val stations: List<StationLocation>? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
