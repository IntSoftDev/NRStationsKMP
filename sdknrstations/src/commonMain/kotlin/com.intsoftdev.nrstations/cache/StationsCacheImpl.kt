package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.UpdateVersion
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock

internal class StationsCacheImpl(
    private val dbWrapper: DBWrapper,
    private val settings: Settings,
    private val clock: Clock
) : StationsCache {

    override suspend fun insertStations(stations: List<StationLocation>) {
        Napier.d("insertAll enter")
        dbWrapper.insertStations(stations)
        setLastUpdateTime()
        Napier.d("insertAll exit")
    }

    override fun insertVersion(version: UpdateVersion) {
        dbWrapper.insertVersion(version)
    }

    override fun getAllStations(): List<StationLocation> {
        Napier.d("getAllStations enter")
        return dbWrapper.getStations()
    }

    override fun getStationLocation(crsCode: String): StationLocation {
        return dbWrapper.getStationLocation(crsCode)
            ?: throw IllegalStateException("station code $crsCode not found")
    }

    override fun getCacheState(serverVersion: Double?): CacheState {
        // 1. is cache empty
        if (isCacheEmpty()) return CacheState.Empty

        // 2. is higher data version available
        serverVersion?.let {
            if (it > getVersion().version) {
                return CacheState.Stale
            }
        }

        // 3. has cache time expiry reached
        return if (doUpdate())
            CacheState.Stale

        // 4. else cache is ok
        else CacheState.Usable
    }

    private fun isCacheEmpty(): Boolean = dbWrapper.isEmpty()

    override fun getVersion(): UpdateVersion {
        return dbWrapper.getVersion()
            ?: throw IllegalStateException("no version in cache")
    }

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
