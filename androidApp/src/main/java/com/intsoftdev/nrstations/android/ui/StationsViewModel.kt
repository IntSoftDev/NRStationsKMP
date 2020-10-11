package com.intsoftdev.nrstations.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.StationsClient
import kotlinx.coroutines.launch

class StationsViewModel(private var stationsClient : StationsClient) : ViewModel() {

    init {
        Napier.d("init")
    }

    fun test() {
        Napier.d("test enter")
        viewModelScope.launch {
            stationsClient.getAllStations()
        }
        
        Napier.d("test exit")
    }
}