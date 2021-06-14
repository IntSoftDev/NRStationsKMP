package com.intsoftdev.nrstations.app.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Kermit
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.sdk.NREStationsSDK
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StationsViewModel(private var stationsSDK: NREStationsSDK, private var logger: Kermit) :
    ViewModel() {

    val stationsLiveData = MutableLiveData<List<StationLocation>>()
    val errorLiveData = MutableLiveData<String>()

    fun getAllStations() {
        viewModelScope.launch {
            stationsSDK.getAllStations()
                .catch { throwable ->
                    errorLiveData.postValue(throwable.message)
                }.collect { stationsResult ->
                    when (stationsResult) {
                        is StationsResultState.Success -> {
                            logger.d { "got stations ${stationsResult.data.stations.count()}" }
                            stationsLiveData.postValue(stationsResult.data.stations)
                        }
                        is StationsResultState.Failure -> {
                            errorLiveData.postValue(stationsResult.error?.message)
                            logger.e { "error" }
                        }
                    }
                }
        }
    }
}