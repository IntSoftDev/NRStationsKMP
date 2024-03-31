package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.toDataVersion
import com.intsoftdev.nrstations.data.model.station.toStationLocation
import com.intsoftdev.nrstations.data.model.station.toVersion
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock

enum class CachePolicy {
    FORCE_REFRESH,
    ALWAYS_USE_CACHE,
    USE_CACHE_WITH_EXPIRY
}

internal class StationsCacheImpl(
    private val dbWrapper: DBWrapper,
    private val settings: Settings,
    private val clock: Clock
) : StationsCache {

    override fun insertStations(stations: List<StationLocation>) {
        Napier.d("insertAll enter")
        dbWrapper.insertStations(stations)
        setLastUpdateTime()
        Napier.d("insertAll exit")
    }

    override fun insertVersion(version: DataVersion) {
        Napier.d("insertVersion $version")
        dbWrapper.insertVersion(version.toVersion())
    }

    override fun getAllStations(): List<StationLocation> {
        Napier.d("getAllStations enter")
        return dbWrapper.getStations().map {
            it.toStationLocation()
        }
    }

    override fun getStationLocation(crsCode: String): StationLocation {
        return dbWrapper.getStationLocation(crsCode)?.toStationLocation()
            ?: throw IllegalStateException("station code $crsCode not found")
    }

    override fun getCacheState(serverVersion: Double?, cachePolicy: CachePolicy): CacheState {
        Napier.d("getCacheState serverVersion $serverVersion cachePolicy $cachePolicy")

        // is cache empty
        if (isCacheEmpty()) return CacheState.Empty

        // force refresh the cache
        if (cachePolicy == CachePolicy.FORCE_REFRESH) {
            return CacheState.Stale
        }

        // is higher data version available
        serverVersion?.let {
            if (it > getVersion().version) {
                return CacheState.Stale
            }
        }

        // use cache regardless of expiry
        if (cachePolicy == CachePolicy.ALWAYS_USE_CACHE) {
            return CacheState.Usable
        }

        // has cache time expiry reached
        return if (doUpdate()) {
            CacheState.Stale
        }

        // else cache is ok
        else {
            CacheState.Usable
        }
    }

    private fun isCacheEmpty(): Boolean = dbWrapper.isEmpty()

    override fun getVersion(): DataVersion =
        dbWrapper.getVersion()?.toDataVersion()
            ?: throw IllegalStateException("no version in cache")

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
