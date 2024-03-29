package com.intsoftdev.nrstations.app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intsoftdev.nrservices.app.ui.theme.NRStationsTheme
import com.intsoftdev.nrstations.app.R
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.viewmodels.SearchableStationViewModel
import com.intsoftdev.nrstations.viewmodels.StationsUiState
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun NRStationsScreen(
    stationsViewModel: SearchableStationViewModel,
    modifier: Modifier = Modifier,
    onSelectionChanged: (StationLocation) -> Unit = {}
) {
    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val stationsUiState by stationsViewModel.uiState.collectAsState()

    val searchText by stationsViewModel.searchText.collectAsState()
    val isSearching by stationsViewModel.isSearching.collectAsState()
    val stationsList by stationsViewModel.stationsList.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchText, // text shown on SearchBar
            onQueryChange = stationsViewModel::onSearchTextChange, // update the value of searchText
            onSearch = {
                stationsViewModel.onToogleSearch()
            }, // the callback to be invoked when the input service triggers the ImeAction.Search action
            active = isSearching, // whether the user is searching or not
            onActiveChange = { stationsViewModel.onToogleSearch() }, // the callback to be invoked when this search bar's active state is changed
            placeholder = {
                Text(text = "Enter station name or CRS code")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (isSearching) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchText.isNotEmpty()) {
                                stationsViewModel.onSearchTextChange("")
                            } else {
                                stationsViewModel.onToogleSearch()
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {
            LazyColumn {
                items(stationsList) { station ->
                    StationRow(station = station) {
                        stationsViewModel.setSelectedStation(station)
                        stationsViewModel.onToogleSearch()
                        onSelectionChanged(station)
                    }
                }
            }
        }

        when (stationsUiState) {
            is StationsUiState.Loading -> {
                LoadingScreen(modifier = modifier.fillMaxSize())
            }

            is StationsUiState.Loaded -> {
                val allStations = (stationsUiState as StationsUiState.Loaded).stations
                stationsViewModel.setAllStations(allStations)

                ResultScreen(
                    stations = allStations,
                    onClick = { stationLocation ->
                        Napier.d("onSelectionChanged ${stationLocation.stationName}")
                        stationsViewModel.setSelectedStation(stationLocation)
                        onSelectionChanged(stationLocation)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            is StationsUiState.Error -> {
                ErrorScreen(modifier = modifier.fillMaxSize()) {
                    stationsViewModel.getAllStations()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        stationsViewModel.getAllStations()
    }
}

/**
 * The stations screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading"
    )
}

/**
 * The stations screen displaying error message with retry button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = "Load stations failed!", modifier = Modifier.padding(16.dp))
        Button(
            onClick = onRetry
        ) {
            Text(text = "Retry")
        }
    }
}

/**
 * ResultScreen displaying stations
 */
@Composable
fun ResultScreen(
    stations: List<StationLocation>,
    onClick: (StationLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(stations) { station ->
            StationRow(station) { stationLocation ->
                onClick(stationLocation)
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    NRStationsTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    NRStationsTheme {
        ErrorScreen {
            // Retry
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationRowPreview() {
    NRStationsTheme {
        StationRow(
            station = StationLocation("London Waterloo", "WAT", 0.0, 0.0)
        ) {}
    }
}

@Composable
fun StationRow(station: StationLocation, onClick: (StationLocation) -> Unit) {
    Row(
        Modifier
            .clickable {
                Napier.d("onClick ${station.stationName}")
                onClick(station)
            }
            .padding(10.dp)
    ) {
        Text(
            text = station.stationName,
            fontSize = 16.sp,
            modifier = Modifier.weight(0.9F)
        )

        Text(
            text = station.crsCode,
            fontSize = 16.sp,
            modifier = Modifier.weight(0.1F)
        )
    }
}
