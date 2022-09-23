package com.intsoftdev.nrstations.shared

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StationsViewModel(
    private var stationsSDK: NREStationsSDK,
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
            stationsSDK.getAllStations()
                .catch { throwable ->
                    _uiState.emit(StationsViewState.Error(throwable))
                }.collect { stationsResult ->
                    when (stationsResult) {
                        is StationsResultState.Success -> {
                            Napier.d("got stations count ${stationsResult.data.stations.size}")
                            _uiState.emit(
                                StationsViewState.StationsLoaded(stationsResult.data.stations)
                            )
                        }
                        is StationsResultState.Failure -> {
                            _uiState.emit(StationsViewState.Error(stationsResult.error))
                            Napier.e("error")
                        }
                    }
                }
        }
    }
}

sealed class StationsViewState {
    object Loading : StationsViewState()
    data class StationsLoaded(val stationsData: List<StationLocation>) : StationsViewState()
    data class Error(val throwable: Throwable?) : StationsViewState()
}
