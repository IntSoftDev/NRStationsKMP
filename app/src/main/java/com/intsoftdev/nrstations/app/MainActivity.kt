package com.intsoftdev.nrstations.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.intsoftdev.nrservices.app.ui.theme.NRStationsTheme
import com.intsoftdev.nrstations.app.ui.StationsNavHost
import com.intsoftdev.nrstations.viewmodels.NrStationsViewModel

class MainActivity : AppCompatActivity() {

    private val stationsViewModel: NrStationsViewModel by viewModels()
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
