package com.intsoftdev.nrstations.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.intsoftdev.nrservices.app.ui.theme.NRStationsTheme
import com.intsoftdev.nrstations.app.ui.StationsNavHost
import com.intsoftdev.nrstations.shared.StationsSDKViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val stationsViewModel: StationsSDKViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stationsViewModel.getAllStations()
        setContent {
            NRStationsTheme {
                StationsNavHost(stationsViewModel)
            }
        }
    }
}
