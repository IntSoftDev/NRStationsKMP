package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import com.intsoftdev.nrstations.sdk.StationsSdkDiComponent
import com.intsoftdev.nrstations.sdk.injectStations
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

open class NrNearbyViewModel : KMMBaseViewModel(), StationsSdkDiComponent {

    init {
        Napier.d("init")
    }

    private var stationsSDK = this.injectStations<NrStationsSDK>()

    // Backing property to avoid state updates from other classes
    // consider replacing with MutableSharedFlow if it doesn't re-emit same value
    private val _uiState = MutableStateFlow(NrNearbyViewState(isLoading = true))

    // The UI collects from this StateFlow to get its state updates
    @NativeCoroutinesState
    val uiState: StateFlow<NrNearbyViewState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        NrNearbyViewState(isLoading = true)
    )

    override fun onCleared() {
        Napier.d("onCleared")
        super.onCleared()
    }

    fun getNearbyStations(crsCode: String) {
        Napier.d("getNearbyStations $crsCode enter")
        viewModelScope.coroutineScope.launch {
            stationsSDK.getStationLocation(crsCode)
                .onStart {
                    Napier.d("onStart")
                    _uiState.emit(NrNearbyViewState(isLoading = true))
                }.catch { throwable ->
                    _uiState.emit(NrNearbyViewState(error = throwable.message))
                }.collect { results ->
                    when (results) {
                        is StationsResultState.Success -> {
                            Napier.d("got stationLocation")
                            getNearbyStations(
                                results.data.first().latitude,
                                results.data.first().longitude
                            )
                        }

                        is StationsResultState.Failure -> {
                            _uiState.emit(NrNearbyViewState(error = results.error?.message))
                            Napier.e("error")
                        }
                    }
                }
        }
    }

    fun getNearbyStations(latitude: Double, longitude: Double) {
        Napier.d("getNearbyStations $latitude $longitude enter")
        stationsSDK.getNearbyStations(latitude, longitude)
        viewModelScope.coroutineScope.launch {
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
    val stations: NearestStations? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
