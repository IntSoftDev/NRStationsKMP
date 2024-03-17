package com.intsoftdev.nrstations.app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.viewmodels.NrStationsViewModel
import com.intsoftdev.nrstations.viewmodels.NreStationsViewState
import io.github.aakira.napier.Napier

@Composable
fun StationsNavHost(
    stationsViewModel: NrStationsViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainscreen"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            "mainscreen"
            // TODO add transitions
        ) {
            MainScreen(
                stationsViewModel = stationsViewModel,
                navController = navController,
                onNavigateToNearbyStations = {
                    navController.navigate("nearbystationslist?latitude=${it.latitude}&longitude=${it.longitude}")
                }
            )
        }
        composable(
            route = "nearbystationslist?latitude={latitude}&longitude={longitude}",
            arguments = listOf(
                navArgument("latitude") {
                    type = NavType.FloatType
                },
                navArgument("longitude") {
                    type = NavType.FloatType
                }
            ) // TODO add transitions
        ) {
            val latitude = it.arguments?.getFloat("latitude")
            val longitude = it.arguments?.getFloat("longitude")
            if (latitude != null && longitude != null) {
                NearbyStationsScreen(
                    nrNearbyViewModel = viewModel(),
                    latitude,
                    longitude
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    stationsViewModel: NrStationsViewModel,
    navController: NavHostController,
    onNavigateToNearbyStations: (StationLocation) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareStationsFlow = remember(stationsViewModel.uiState, lifecycleOwner) {
        stationsViewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val stationsState by lifecycleAwareStationsFlow.collectAsState(stationsViewModel.uiState.value)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stations") }
            )
        }
    ) {
        // Screen content
        MainScreenContent(
            stationsState = stationsState,
            onRefresh = { stationsViewModel.getAllStations() },
            onSuccess = { data -> Napier.d("View updating with ${data.size} stations") },
            onError = { exception -> Napier.e { "Displaying error: $exception" } },
            onStationSelect = onNavigateToNearbyStations
        )
    }
}

@Composable
fun MainScreenContent(
    stationsState: NreStationsViewState,
    onRefresh: () -> Unit = {},
    onSuccess: (List<StationLocation>) -> Unit = {},
    onError: (String) -> Unit = {},
    onStationSelect: (StationLocation) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = stationsState.isLoading),
            onRefresh = onRefresh
        ) {
            val stations = stationsState.stations
            if (stations != null) {
                LaunchedEffect(stations) {
                    onSuccess(stations)
                }
                Success(successData = stations, stationSelect = onStationSelect)
            }
            val error = stationsState.error
            if (error != null) {
                LaunchedEffect(error) {
                    onError(error)
                }
                Error(error)
            }
        }
    }
}

@Composable
fun Error(error: String) {
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
fun Success(
    successData: List<StationLocation>,
    stationSelect: (StationLocation) -> Unit
) {
    StationsList(stations = successData, stationSelect)
}

@Composable
fun StationsList(stations: List<StationLocation>, onItemClick: (StationLocation) -> Unit) {
    LazyColumn {
        items(stations) { station ->
            StationRow(station) {
                onItemClick(it)
            }
            Divider()
        }
    }
}

@Composable
fun StationRow(station: StationLocation, onClick: (StationLocation) -> Unit) {
    Row(
        Modifier
            .clickable { onClick(station) }
            .padding(10.dp)
    ) {
        Text(station.stationName, Modifier.weight(1F))
    }
}
