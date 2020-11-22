package com.intsoftdev.nrstations.cache

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.db.NRStationsKMPDb
import com.intsoftdev.nrstations.model.StationModel


class StationsCache(private val nrStationsKMPDb: NRStationsKMPDb) {

    private val nRStationsTableQueries = nrStationsKMPDb.nRStationsTableQueries

    fun insertAll(stations: List<StationModel>) {
        Napier.d("insertAll enter")
        stations.forEach {
            nRStationsTableQueries.insertItem(
                null,
                crsCode = it.crsCode,
                stationName = it.stationName,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
        Napier.d("insertAll exit")
    }

    fun getAllStations(): List<StationModel> {
        val results =
            nRStationsTableQueries.selectAll(mapper = { id, crsCode, stationName, latitude, longitude ->
                StationModel(
                    stationName = stationName,
                    crsCode = crsCode,
                    latitude = latitude,
                    longitude = longitude
                )
            }).executeAsList()

        return results
    }

    fun deleteAll() {
        Napier.d("deleteAll enter")
        nrStationsKMPDb.transaction {
            nRStationsTableQueries.deleteAll()
        }
        Napier.d("deleteAll exit")
    }
}