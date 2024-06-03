package com.intsoftdev.nrstations.app.ui

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationDistance
import com.intsoftdev.nrstations.common.StationLocation
import java.util.Locale

@Composable
internal fun NearbyScreen(
    stationLocation: StationLocation,
    modifier: Modifier = Modifier,
    nearbyViewModel: NearbyStationsViewModel = viewModel()
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val lifecycleAwareStationsFlow =
        remember(nearbyViewModel.uiState, lifecycleOwner) {
            nearbyViewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle)
        }

    LaunchedEffect(Unit) {
        nearbyViewModel.getNearbyStations(stationLocation.latitude, stationLocation.longitude)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val nearbyUiState by lifecycleAwareStationsFlow.collectAsState(nearbyViewModel.uiState.value)

    when (nearbyUiState) {
        is NearbyStationsUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }

        is NearbyStationsUiState.Loaded -> {
            NearbyStationsSuccess(
                nearestStations = (nearbyUiState as NearbyStationsUiState.Loaded).stations,
                referenceStation = stationLocation,
                stationSelect = {
                    // No op
                }
            )
        }

        is NearbyStationsUiState.Error -> {
            ErrorScreen(modifier = modifier.fillMaxSize()) {
                nearbyViewModel.getNearbyStations(
                    stationLocation.latitude,
                    stationLocation.longitude
                )
            }
        }
    }
}

@Composable
fun NearbyStationsSuccess(
    nearestStations: NearestStations,
    referenceStation: StationLocation,
    stationSelect: (StationLocation) -> Unit
) {
    val cameraPositionState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        nearestStations.geolocation.latitude,
                        nearestStations.geolocation.longitude
                    ),
                    15f
                )
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
                modifier =
                    Modifier
                        .fillMaxSize()
                        .testTag("Map"),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                }
            )
            if (!isMapLoaded) {
                androidx.compose.animation.AnimatedVisibility(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    visible = !isMapLoaded,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .wrapContentSize()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .height(maxHeight / 2)
                    .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            val filteredStations =
                nearestStations.stationDistances.filter { it.station.crsCode != referenceStation.crsCode }
            LazyColumn {
                items(filteredStations) { station ->
                    NearbyStationRow(station) {
                        stationSelect(it)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun NearbyStationRow(
    station: StationDistance,
    onClick: (StationLocation) -> Unit
) {
    val distance = String.format(Locale.UK, "%.1f miles", station.distanceInMiles)
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
        // Drawing on the map is accomplished with a child-based API
    }
}
