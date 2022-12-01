package com.intsoftdev.nrstations.app.ui

import StationsSdkDiComponent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.viewmodels.NreStationsViewState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import provide

class StationsViewModel : ViewModel(), StationsSdkDiComponent {

    private val stationsSDK = this.provide<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(NreStationsViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<NreStationsViewState> = _uiState

    fun getAllStations() {
        Napier.d("getStationsFromNetwork enter")
        viewModelScope.launch {
            stationsSDK.getAllStations().onStart {
                _uiState.emit(NreStationsViewState(isLoading = true))
            }.catch { throwable ->
                _uiState.emit(NreStationsViewState(error = throwable.message))
            }.collect { stationsResult ->
                when (stationsResult) {
                    is StationsResultState.Success -> {
                        Napier.d("got stations count ${stationsResult.data.stations.size}")
                        _uiState.emit(
                            NreStationsViewState(stations = stationsResult.data.stations)
                        )
                    }
                    is StationsResultState.Failure -> {
                        _uiState.emit(NreStationsViewState(error = stationsResult.error?.message))
                        Napier.e("error")
                    }
                }
            }
        }

        Napier.d("getStationsFromNetwork exit")
    }
}
