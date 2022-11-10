package com.intsoftdev.nrstations.app.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NearbyStationsScreen() {

    val nearbyStationsViewModel: NearbyStationsViewModel = viewModel()

    Text(text = "Hello ${nearbyStationsViewModel.crsCode} from VM !")
}

@Preview
@Composable
fun PreviewMessageCard() {
    NearbyStationsScreen()
}
