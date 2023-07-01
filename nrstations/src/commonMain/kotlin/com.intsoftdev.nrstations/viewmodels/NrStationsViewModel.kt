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

open class NrStationsViewModel : KMMBaseViewModel(), StationsSdkDiComponent {

    init {
        Napier.d("init")
    }

    private var stationsSDK = this.injectStations<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(NreStationsViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    @NativeCoroutinesState
    val uiState: StateFlow<NreStationsViewState> = _uiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NreStationsViewState(isLoading = true))

    override fun onCleared() {
        Napier.d("onCleared")
        super.onCleared()
    }

    fun getAllStations() {
        Napier.d("getAllStations enter")
        viewModelScope.coroutineScope.launch {
            Napier.d("getAllStations launch")
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
    }
}

data class NreStationsViewState(
    val stations: List<StationLocation>? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
