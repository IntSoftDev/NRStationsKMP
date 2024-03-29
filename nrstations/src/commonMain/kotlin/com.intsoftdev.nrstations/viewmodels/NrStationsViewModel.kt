package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class NrStationsViewModel : KMMViewModel(), StationsSdkDiComponent {

    init {
        Napier.d("init")
    }

    private var stationsSDK = this.injectStations<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow<StationsUiState>((StationsUiState.Loading))

    // The UI collects from this StateFlow to get its state updates
    @NativeCoroutinesState
    val uiState: StateFlow<StationsUiState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        StationsUiState.Loading
    )

    override fun onCleared() {
        Napier.d("onCleared")
        super.onCleared()
    }

    fun getAllStations() {
        Napier.d("getAllStations enter")
        viewModelScope.coroutineScope.launch {
            Napier.d("getAllStations launch")
            stationsSDK.getAllStations().onStart {
                _uiState.emit(StationsUiState.Loading)
            }.catch { throwable ->
                _uiState.emit(StationsUiState.Error(error = throwable.message))
            }.collect { stationsResult ->
                when (stationsResult) {
                    is StationsResultState.Success -> {
                        Napier.d("got stations count ${stationsResult.data.stations.size}")
                        _uiState.emit(
                            StationsUiState.Loaded(stations = stationsResult.data.stations)
                        )
                    }

                    is StationsResultState.Failure -> {
                        _uiState.emit(StationsUiState.Error(error = stationsResult.error?.message))
                        Napier.e("error")
                    }
                }
            }
        }
    }
}

sealed interface StationsUiState {
    data object Loading : StationsUiState
    data class Error(val error: String?) : StationsUiState
    data class Loaded(
        val stations: List<StationLocation>,
        val lastUpdateText: String? = null
    ) : StationsUiState
}
