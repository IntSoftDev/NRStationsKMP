package com.intsoftdev.nrstations.android.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.StationsClient
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.shared.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StationsViewModel(private var stationsClient : StationsClient) : ViewModel() {

    val stationsLiveData = MutableLiveData<List<StationModel>>()
    val errorLiveData = MutableLiveData<String>()

    fun getStationsFromNetwork() {
        Napier.d("getStationsFromNetwork enter")
        viewModelScope.launch {
            stationsClient.getAllStations().also {
                when(it) {
                    is ResultState.Success -> stationsLiveData.postValue(it.data)
                    is ResultState.Failure -> {
                        errorLiveData.postValue(it.error?.message)
                        Napier.e("error")
                    }
                }
            }
        }
        
        Napier.d("getStationsFromNetwork exit")
    }
}