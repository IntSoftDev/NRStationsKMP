package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import com.intsoftdev.nrstations.common.StationsResultState
import kotlinx.coroutines.launch

class StationsViewModel(private var stationsSDK : NREStationsSDK) : ViewModel() {

    val stationsLiveData = MutableLiveData<List<StationLocation>>()
    val errorLiveData = MutableLiveData<String>()

    fun getStationsFromNetwork() {
        Napier.d("getStationsFromNetwork enter")
        viewModelScope.launch {
            stationsSDK.getAllStations().also {
                when(it) {
                    is StationsResultState.Success -> stationsLiveData.postValue(it.data.stations)
                    is StationsResultState.Failure -> {
                        errorLiveData.postValue(it.error?.message)
                        Napier.e("error")
                    }
                }
            }
        }
        
        Napier.d("getStationsFromNetwork exit")
    }
}