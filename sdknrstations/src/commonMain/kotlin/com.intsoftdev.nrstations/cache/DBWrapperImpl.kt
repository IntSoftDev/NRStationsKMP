package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.model.station.toNRStation
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.database.Station
import com.intsoftdev.nrstations.database.Version
import io.github.aakira.napier.Napier

internal class DBWrapperImpl(
    private val stationsDb: NRStationsDb
) : DBWrapper {

    override fun insertStation(stations: Station) {
        stationsDb.stationsTableQueries.insert(
            stations.crs,
            stations.name,
            stations.latitude,
            stations.longitude
        )
    }

    override fun insertStations(stations: List<StationLocation>) {
        stationsDb.stationsTableQueries.transaction {
            stations.map {
                with(it.toNRStation()) {
                    stationsDb.stationsTableQueries.insert(
                        this.crs,
                        this.name,
                        this.latitude,
                        this.longitude
                    )
                }
            }
        }
    }

    override fun getStations(): List<Station> =
        stationsDb.stationsTableQueries.selectAll().executeAsList()

    override fun getStationLocation(stationId: String): Station? {
        return getStations().firstOrNull { it.crs == stationId }
    }

    override fun insertVersion(version: Version) {
        Napier.d("insertVersion $version")
        // first delete any rows as there should be only one version instance
        stationsDb.versionTableQueries.deleteAll()
        // insert the new row
        stationsDb.versionTableQueries.insert(version.version, version.lastUpdate)
    }

    override fun getVersion(): Version? =
        stationsDb.versionTableQueries.select().executeAsOneOrNull()

    override fun isEmpty(): Boolean {
        return stationsDb.stationsTableQueries.selectAll().executeAsList().isEmpty()
    }
}
