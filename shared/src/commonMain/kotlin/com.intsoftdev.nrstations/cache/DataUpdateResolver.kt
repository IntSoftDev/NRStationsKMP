package com.intsoftdev.nrstations.cache

import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock


sealed class DataUpdateAction {
    object REFRESH : DataUpdateAction()
    object LOCAL : DataUpdateAction()
}

class DataUpdateResolver(private val settings: Settings, private val clock: Clock) {

    companion object {
        private const val EXPIRATION_TIME_MS = 12 * 60 * 60 * 1000 // 12 hrs
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
    }

    fun setLastUpdateTime() {
        val lastUpdateMS = clock.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, lastUpdateMS)
    }

    private fun doUpdate(currentTimeMS: Long): Boolean {
        val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
        return currentTimeMS - lastDownloadTimeMS > Companion.EXPIRATION_TIME_MS
    }

    fun getUpdateAction(stationsCache: StationsCache): DataUpdateAction {
        if (stationsCache.isCacheEmpty()) return DataUpdateAction.REFRESH
        val currentTimeMS = clock.now().toEpochMilliseconds()
        return if (doUpdate(currentTimeMS))
            DataUpdateAction.REFRESH
        else DataUpdateAction.LOCAL
    }
}