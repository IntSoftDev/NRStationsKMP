package com.intsoftdev.nrstations.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.intsoftdev.nrstations.app.ui.NRStationsScreen
import com.intsoftdev.nrstations.app.ui.NearbyStationsScreen
import com.intsoftdev.nrstations.viewmodels.SearchableStationViewModel


/**
 * enum values that represent the screens in the app
 */
enum class StationsScreen(val title: String) {
    Search("Search"),
    Nearby("Nearby")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NRStationsApp(
    stationsViewModel: SearchableStationViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = StationsScreen.valueOf(
        backStackEntry?.destination?.route ?: StationsScreen.Search.name
    )

    Scaffold(
        topBar = {
            StationsTopAppBar(
                scrollBehavior = scrollBehavior,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = StationsScreen.Search.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = StationsScreen.Search.name) {
                NRStationsScreen(
                    stationsViewModel = stationsViewModel,
                    onSelectionChanged = {
                        navController.navigate(StationsScreen.Nearby.name)
                    }
                )
            }
            composable(route = StationsScreen.Nearby.name) {
                NearbyStationsScreen(
                    latitude = stationsViewModel.selectedStation.value!!.latitude.toFloat(),
                    longitude = stationsViewModel.selectedStation.value!!.longitude.toFloat()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "NR Stations",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        },
        modifier = modifier
    )
}
