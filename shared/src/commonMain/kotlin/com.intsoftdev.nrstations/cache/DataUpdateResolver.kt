package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.shared.currentTimeMillis
import com.russhwolf.settings.Settings


sealed class DataUpdateAction {
    object REFRESH : DataUpdateAction()
    object LOCAL : DataUpdateAction()
}

class DataUpdateResolver(private val settings: Settings) {

    companion object {
        private const val EXPIRATION_TIME_MS = (12 * 60 * 60 * 1000).toLong() // 12 hrs
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
    }

    fun setLastUpdateTime(lastUpdateMS: Long) {
        settings.putLong(DB_TIMESTAMP_KEY, lastUpdateMS)
    }

    private fun doUpdate(currentTimeMS: Long): Boolean {
        val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
        return currentTimeMS - lastDownloadTimeMS > Companion.EXPIRATION_TIME_MS
    }

    fun getUpdateAction(stationsCache: StationsCache): DataUpdateAction {
        if (stationsCache.isCacheEmpty()) return DataUpdateAction.REFRESH
        val currentTimeMS = currentTimeMillis()
        return if (doUpdate(currentTimeMS))
            DataUpdateAction.REFRESH
        else DataUpdateAction.LOCAL
    }
}