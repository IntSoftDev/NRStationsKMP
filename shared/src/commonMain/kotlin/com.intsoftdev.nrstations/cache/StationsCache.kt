package com.intsoftdev.nrstations.cache

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsList
import org.kodein.db.DB
import org.kodein.db.find
import org.kodein.db.useModels

class StationsCache(private val db: DB) {

    fun insertAll(stations: List<StationModel>) {
        Napier.d("insertAll enter")
        val stationListData = StationsList("stationList", stations)
        db.put(stationListData)
        Napier.d("insertAll exit")
    }

    fun getAllStations(): List<StationModel> {
        Napier.d("getAllStations enter")
        val stationsList = db.find<StationsList>().all().useModels {it.first()}
        return stationsList.stations.also {
            Napier.d("getAllStations exit")
        }
    }
}