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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.intsoftdev.nrstations.app.ui.NRStationsScreen
import com.intsoftdev.nrstations.app.ui.NearbyScreen
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
fun NRStationsDemoApp(
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

    val title = remember {
        mutableStateOf("Home")
    }

    val selectedStation by stationsViewModel.selectedStation.collectAsState()

    Scaffold(
        topBar = {
            StationsTopAppBar(
                scrollBehavior = scrollBehavior,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                },
                title = title
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
                        title.value = "Stations near ${it.stationName}"
                        navController.navigate(StationsScreen.Nearby.name)
                    }
                )
            }
            composable(route = StationsScreen.Nearby.name) {
                with(selectedStation) {
                    requireNotNull(this)
                    NearbyScreen(
                        stationLocation = this
                    )
                }
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
    title: State<String>,
    modifier: Modifier = Modifier
) {
    val style = if (canNavigateBack) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineSmall
    val topBarTitle = if (canNavigateBack) title.value else "NR Stations"
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = topBarTitle,
                style = style
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
