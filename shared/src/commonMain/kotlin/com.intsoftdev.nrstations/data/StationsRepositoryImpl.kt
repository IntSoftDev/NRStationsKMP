package com.intsoftdev.nrstations.data

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel

class StationsRepositoryImpl(private val stationsProxyService: StationsProxyService) :
    StationsRepository {

    override suspend fun getAllStations(): List<StationModel> {
        return stationsProxyService.getAllStations().also {
            it.forEach {
                Napier.d(" ${it.stationName}")
            }
        }
    }

    override fun saveAllStations(version: DataVersion, stations: List<StationModel>) {
        TODO("Not yet implemented")
    }

    override fun getModelFromCache(stationName: String?, crsCode: String?): StationModel {
        TODO("Not yet implemented")
    }
}