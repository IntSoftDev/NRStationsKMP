package com.intsoftdev.nrstations.cache

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.model.StationsVersion
import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock

internal class StationsCacheImpl(
    private val dbWrapper: DBWrapper,
    private val settings: Settings,
    private val clock: Clock
) : StationsCache {

    override fun insertStations(stations: List<StationModel>) {
        Napier.d("insertAll enter")
        val stationListData = StationsList("stationList", stations)
        dbWrapper.insertStations(stationListData)
        setLastUpdateTime()
        Napier.d("insertAll exit")
    }

    override fun insertVersion(version: DataVersion) {
        val versionData = StationsVersion("stationsVersion", version.version, version.lastUpdated)
        dbWrapper.insertVersion(versionData)
    }

    override fun getAllStations(): List<StationModel>? {
        Napier.d("getAllStations enter")
        val stationsList = dbWrapper.getStations()
        return stationsList?.stations.also {
            Napier.d("getAllStations exit")
        }
    }

    override fun isCacheEmpty(): Boolean {
        return dbWrapper.isEmpty()
    }

    override fun getVersion(): StationsVersion? {
        return dbWrapper.getVersion()
    }

    override fun getUpdateAction(): DataUpdateAction {
        if (isCacheEmpty()) return DataUpdateAction.REFRESH
        val currentTimeMS = clock.now().toEpochMilliseconds()
        return if (doUpdate(currentTimeMS))
            DataUpdateAction.REFRESH
        else DataUpdateAction.LOCAL
    }

    private fun setLastUpdateTime() {
        val lastUpdateMS = clock.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, lastUpdateMS)
    }

    private fun doUpdate(currentTimeMS: Long): Boolean {
        val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
        return currentTimeMS - lastDownloadTimeMS > EXPIRATION_TIME_MS
    }

    companion object {
        private const val EXPIRATION_TIME_MS = 12 * 60 * 60 * 1000 // 12 hrs
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
    }
}