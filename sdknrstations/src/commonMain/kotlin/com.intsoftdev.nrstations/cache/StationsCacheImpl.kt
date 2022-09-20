package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock

internal class StationsCacheImpl(
    private val settings: Settings,
    private val clock: Clock
) : StationsCache {

    override fun insertStations(stations: List<StationLocation>) {
        Napier.d("insertAll enter")
        setLastUpdateTime()
        Napier.d("insertAll exit")
    }

    override fun insertVersion(version: DataVersion) {
        Napier.d("insertVersion $version")
    }

    override fun getAllStations(): List<StationLocation> {
        Napier.d("getAllStations enter")
        throw IllegalStateException("method not supported")
    }

    override fun getStationLocation(crsCode: String): StationLocation {
        throw IllegalStateException("method not supported")
    }

    override fun getCacheState(serverVersion: Double?): CacheState {
        return CacheState.Empty
    }

    private fun isCacheEmpty(): Boolean = true

    override fun getVersion(): DataVersion =
        throw IllegalStateException("no version in cache")

    private fun setLastUpdateTime() {
        val lastUpdateMS = clock.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, lastUpdateMS)
    }

    private fun doUpdate(): Boolean {
        val currentTimeMS = clock.now().toEpochMilliseconds()
        val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
        return currentTimeMS - lastDownloadTimeMS > EXPIRATION_TIME_MS
    }

    companion object {
        private const val EXPIRATION_TIME_MS = 12 * 60 * 60 * 1000 // 12 hrs
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
    }
}
