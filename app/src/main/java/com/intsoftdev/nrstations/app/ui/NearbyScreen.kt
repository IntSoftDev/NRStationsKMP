package com.intsoftdev.nrstations.app.ui

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.flowWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.intsoftdev.nrstations.common.Geolocation
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsDistance
import com.intsoftdev.nrstations.viewmodels.NreNearbyViewState
import io.github.aakira.napier.Napier
import java.util.Locale

@Composable
internal fun NearbyStationsScreen(
    nearbyStationsViewModel: NearbyStationsViewModel
) {
    Napier.d("NearbyStationsScreen enter")
    val lifecycleOwner = LocalLifecycleOwner.current

    val lifecycleAwareStationsFlow = remember(nearbyStationsViewModel.uiState, lifecycleOwner) {
        nearbyStationsViewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    LaunchedEffect(Unit) {
        nearbyStationsViewModel.getNearbyStations()
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val stationsState by lifecycleAwareStationsFlow.collectAsState(nearbyStationsViewModel.uiState.value)

    NearbyScreenContent(
        stationsState = stationsState,
        onRefresh = { Napier.d("onRefresh") },
        onSuccess = { data -> Napier.d("View updating with ${data.size} stations") },
        onError = { exception -> Napier.e { "Displaying error: $exception" } },
        onStationSelect = { Napier.d("onStationSelect") }
    )
}

@Composable
private fun NearbyScreenContent(
    stationsState: NreNearbyViewState,
    onRefresh: () -> Unit = {},
    onSuccess: (List<StationsDistance>) -> Unit = {},
    onError: (String) -> Unit = {},
    onStationSelect: (StationLocation) -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val isLoading = stationsState.isLoading
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .wrapContentSize()
            )
        } else {
            val nearbyStations = stationsState.stations?.values?.firstOrNull()
            val geolocation = stationsState.stations?.keys?.firstOrNull()
            if (nearbyStations != null && geolocation != null) {
                LaunchedEffect(nearbyStations) {
                    onSuccess(nearbyStations)
                }
                NearbyStationsSuccess(
                    geolocation = geolocation,
                    successData = nearbyStations,
                    stationSelect = onStationSelect
                )
            }
            val error = stationsState.error
            if (error != null) {
                LaunchedEffect(error) {
                    onError(error)
                }
                NearbyError(error)
            }
        }
    }
}

@Composable
fun NearbyStationsSuccess(
    geolocation: Geolocation,
    successData: List<StationsDistance>,
    stationSelect: (StationLocation) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(geolocation.latitude, geolocation.longitude), 15f)
    }
    var isMapLoaded by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(10.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(maxHeight / 2)
        ) {
            GoogleMapViewInColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("Map"),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                }
            )
            if (!isMapLoaded) {
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxSize(),
                    visible = !isMapLoaded,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .wrapContentSize()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .height(maxHeight / 2)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            NearbyStationsList(stations = successData, stationSelect)
        }
    }
}

@Composable
fun NearbyStationsList(stations: List<StationsDistance>, onItemClick: (StationLocation) -> Unit) {
    LazyColumn {
        items(stations) { station ->
            NearbyStationRow(station) {
                onItemClick(it)
            }
            Divider()
        }
    }
}

@Composable
fun NearbyStationRow(station: StationsDistance, onClick: (StationLocation) -> Unit) {
    val distance = String.format(Locale.UK, "%.1f miles", station.distanceFromRefInMiles)
    Row(
        Modifier
            .clickable { onClick(station.station) }
            .padding(10.dp)
    ) {
        Text(station.station.stationName, Modifier.weight(0.8F))
        Text(distance, Modifier.weight(0.2F), fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun NearbyError(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = error)
    }
}

@Composable
private fun GoogleMapViewInColumn(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit
) {
    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded
    ) {
        // TODO if needed
        // Drawing on the map is accomplished with a child-based API
    }
}
