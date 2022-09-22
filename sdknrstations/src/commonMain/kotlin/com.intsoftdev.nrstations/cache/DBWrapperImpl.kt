package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.entities.StationDb
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.UpdateVersion
import com.intsoftdev.nrstations.shared.getApplicationFilesDirectoryPath
import io.github.aakira.napier.Napier
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

internal class DBWrapperImpl : DBWrapper {
    private var realm: Realm

    init {
        val path = getApplicationFilesDirectoryPath()
        Napier.d("creating realm config with path $path")
        val config = RealmConfiguration.Builder(
            schema = setOf(StationDb::class)
        ).directory(path).build()
        Napier.d("created realm config")
        // Open Realm
        realm = Realm.open(config)

        Napier.d("opened realm")
    }

    override suspend fun insertStations(stations: List<StationLocation>) {
        realm.write {
            stations.forEach { station ->
                copyToRealm(
                    StationDb().apply {
                        stationName = station.stationName
                        crsCode = station.crsCode
                        latitude = station.latitude
                        longitude = station.longitude
                    },
                    updatePolicy = UpdatePolicy.ALL
                )
            }
        }
    }

    // don't call find() from UI thread
    override fun getStations(): List<StationLocation> {
        return realm.query<StationDb>().find().map {
            StationLocation(it.stationName, it.crsCode, it.latitude, it.longitude)
        }
    }

    override fun getStationLocation(stationId: String): StationLocation? {
        val query = realm.query<StationDb>("crsCode = $0", stationId)
        return when (val station = query.find().firstOrNull()) {
            null -> null
            else -> {
                StationLocation(
                    station.stationName,
                    station.crsCode,
                    station.latitude,
                    station.longitude
                )
            }
        }
    }

    override fun insertVersion(version: UpdateVersion) {
        // db.put(version)
    }

    override fun getVersion(): UpdateVersion {
        // TODO
        return UpdateVersion(0.0, 0L)
    }

    override fun isEmpty(): Boolean {
        return realm.query<StationDb>().find().isEmpty()
    }
}
