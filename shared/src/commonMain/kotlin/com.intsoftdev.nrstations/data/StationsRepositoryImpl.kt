package com.intsoftdev.nrstations.data

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.DataUpdateAction
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.model.StationsVersion
import com.intsoftdev.nrstations.shared.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class StationsRepositoryImpl(
    private val stationsProxyService: StationsServiceAPI,
    private val stationsCache: StationsCache,
    private val requestDispatcher: CoroutineDispatcher
) : StationsRepository {

    override suspend fun getAllStations(): ResultState<StationsResult> {
        return runCatching {
            withContext(requestDispatcher) {
                when (stationsCache.getUpdateAction()) {
                    is DataUpdateAction.REFRESH -> {
                        ResultState.Success(refreshStations())
                    }
                    is DataUpdateAction.LOCAL -> {
                        ResultState.Success(getStationsFromCache())
                    }
                }
            }
        }.getOrElse { throwable ->
            ResultState.Failure(throwable)
        }
    }

    private suspend fun getServerDataVersion(): DataVersion {
        return stationsProxyService.getDataVersion().first()
    }

    private fun isCachedVersionSuperseded(serverDataVersion: DataVersion): Boolean {
        return getVersionFromCache()?.let { cachedData ->
            serverDataVersion.version > cachedData.version
        } ?: true
    }

    override fun saveResult(stationsResult: StationsResult) {
        Napier.d("saveAllStations version ${stationsResult.version} count ${stationsResult.stations.size}")
        stationsCache.insertStations(stationsResult.stations)
        stationsCache.insertVersion(stationsResult.version)
    }

    override fun getAllStationsFromCache(): List<StationModel> {
        Napier.d("getAllStationsFromCache")
        return stationsCache.getAllStations() ?: throw IllegalStateException("empty stations cache")
    }

    override fun getVersionFromCache(): StationsVersion? = stationsCache.getVersion()

    private suspend fun refreshStations(): StationsResult {
        val serverDataVersion = getServerDataVersion()
        return if (isCachedVersionSuperseded(serverDataVersion)) {
            val stations = stationsProxyService.getAllStations()
            StationsResult(
                version = serverDataVersion,
                stations = stations
            ).also { stationsResult ->
                saveResult(stationsResult)
            }
        } else {
            getStationsFromCache()
        }
    }

    private fun getStationsFromCache() =
        StationsResult(
            version = DataVersion(
                getVersionFromCache()?.version
                    ?: throw IllegalStateException(""), 0
            ),
            stations = getAllStationsFromCache()
        )
}