package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.rickclephas.kmp.observableviewmodel.stateIn
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class NrNearbyViewModel : KMMBaseViewModel(), StationsSdkDiComponent {
    private val stationsSDK = this.injectStations<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow<NearbyUiState>(NearbyUiState.Loading)

    // The UI collects from this StateFlow to get its state updates
    @NativeCoroutinesState
    val uiState: StateFlow<NearbyUiState> =
        _uiState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            NearbyUiState.Loading
        )

    init {
        Napier.d(tag = TAG) { "init" }
    }

    override fun onCleared() {
        Napier.d(tag = TAG) { "onCleared" }
        super.onCleared()
    }

    @Suppress("unused")
    fun getNearbyStations(crsCode: String) {
        viewModelScope.coroutineScope.launch {
            stationsSDK.getStationLocation(crsCode)
                .onStart {
                    _uiState.emit(NearbyUiState.Loading)
                }.catch { throwable ->
                    _uiState.emit(NearbyUiState.Error(error = throwable.message))
                }.collect { results ->
                    when (results) {
                        is StationsResultState.Success -> {
                            getNearbyStations(
                                results.data.first().latitude,
                                results.data.first().longitude
                            )
                        }

                        is StationsResultState.Failure -> {
                            _uiState.emit(NearbyUiState.Error(error = results.error?.message))
                            Napier.e(tag = TAG) { "${results.error}" }
                        }
                    }
                }
        }
    }

    fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.coroutineScope.launch {
            stationsSDK.getNearbyStations(latitude, longitude)
                .onStart {
                    _uiState.emit(NearbyUiState.Loading)
                }.catch { throwable ->
                    _uiState.emit(NearbyUiState.Error(error = throwable.message))
                }.collect { result ->
                    when (result) {
                        is StationsResultState.Success -> {
                            Napier.d(tag = TAG) { "got stations count ${result.data.stationDistances.size}" }
                            _uiState.emit(
                                NearbyUiState.Loaded(stations = result.data)
                            )
                        }

                        is StationsResultState.Failure -> {
                            _uiState.emit(NearbyUiState.Error(error = result.error?.message))
                            Napier.e(tag = TAG) { " error: ${result.error?.message} " }
                        }
                    }
                }
        }
    }

    companion object {
        const val TAG = "NrNearbyViewModel"
    }
}

sealed interface NearbyUiState {
    data object Loading : NearbyUiState

    data class Error(val error: String?) : NearbyUiState

    data class Loaded(
        val stations: NearestStations
    ) : NearbyUiState
}
