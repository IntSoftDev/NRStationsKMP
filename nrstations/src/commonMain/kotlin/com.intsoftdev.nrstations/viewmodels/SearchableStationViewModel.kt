package com.intsoftdev.nrstations.viewmodels

import com.intsoftdev.nrstations.common.StationLocation
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class SearchableStationViewModel : NrStationsViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedStation = MutableStateFlow<StationLocation?>(null)
    val selectedStation = _selectedStation.asStateFlow()

    private val allStations = mutableListOf<StationLocation>()

    private val _stationsList = MutableStateFlow(allStations)

    fun setSelectedStation(stationLocation: StationLocation) {
        _selectedStation.value = stationLocation
    }

    fun setAllStations(stationLocations: List<StationLocation>) {
        allStations.clear()
        allStations.addAll(stationLocations)
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    val stationsList =
        searchText
            .combine(_stationsList) { text, stations -> // combine searchText with _stationsList
                if (text.isBlank()) {
                    stations
                } else {
                    stations.filter { station ->
                        // filter and return a list of stations using the Crs code or station name
                        (text.isCrsCode(station.crsCode) || station.stationName.startsWith(text, ignoreCase = true))
                    }
                }
            }.stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = _stationsList.value
            )

    private fun String.isCrsCode(crsCode: String) = count() == 3 && crsCode == uppercase()
}
