package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.rickclephas.kmp.observableviewmodel.stateIn
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class NrStationsViewModel :
    ViewModel(),
    StationsSdkDiComponent {
    private var stationsSDK = this.injectStations<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow<StationsUiState>((StationsUiState.Loading))

    // The UI collects from this StateFlow to get its state updates
    @NativeCoroutinesState
    val uiState: StateFlow<StationsUiState> =
        _uiState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            StationsUiState.Loading
        )

    init {
        Napier.d(tag = TAG) { "init" }
    }

    override fun onCleared() {
        Napier.d(tag = TAG) { "onCleared" }
        super.onCleared()
    }

    fun getAllStations(cachePolicy: CachePolicy = CachePolicy.USE_CACHE_WITH_EXPIRY) {
        viewModelScope
            .coroutineScope
            .launch {
                stationsSDK
                    .getAllStations(cachePolicy = cachePolicy)
                    .onStart {
                        _uiState.emit(StationsUiState.Loading)
                    }.catch { throwable ->
                        _uiState.emit(StationsUiState.Error(error = throwable.message))
                    }.collect { stationsResult ->
                        when (stationsResult) {
                            is StationsResultState.Success -> {
                                _uiState.emit(
                                    StationsUiState.Loaded(stations = stationsResult.data.stations)
                                )
                            }

                            is StationsResultState.Failure -> {
                                _uiState.emit(StationsUiState.Error(error = stationsResult.error?.message))
                                Napier.e("${stationsResult.error}")
                            }
                        }
                    }
            }
    }

    companion object {
        const val TAG = "NrStationsViewModel"
    }
}

sealed interface StationsUiState {
    data object Loading : StationsUiState

    data class Error(
        val error: String?
    ) : StationsUiState

    data class Loaded(
        val stations: List<StationLocation>,
        val lastUpdateText: String? = null
    ) : StationsUiState
}
