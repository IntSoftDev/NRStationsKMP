package com.intsoftdev.nrstations.data

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.DataUpdateAction
import com.intsoftdev.nrstations.cache.DataUpdateResolver
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsResult
import com.intsoftdev.nrstations.model.StationsVersion
import com.intsoftdev.nrstations.shared.ResultState
import com.intsoftdev.nrstations.shared.currentTimeMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class StationsRepositoryImpl(
    private val stationsProxyService: StationsProxyService,
    private val stationsCache: StationsCache,
    private val requestDispatcher: CoroutineDispatcher,
    private val dataUpdateResolver: DataUpdateResolver
) : StationsRepository {

    override suspend fun getAllStations(): ResultState<List<StationModel>> {
        return runCatching {
            withContext(requestDispatcher) {
                when (dataUpdateResolver.getUpdateAction(stationsCache)) {
                    is DataUpdateAction.REFRESH -> {
                        val stationsResult = getVersionThenStations()
                        saveAllStations(stationsResult)
                        dataUpdateResolver.setLastUpdateTime(currentTimeMillis())
                        ResultState.Success(stationsResult.stations)
                    }
                    is DataUpdateAction.LOCAL -> {
                        ResultState.Success(getAllStationsFromCache())
                    }
                }
            }
        }.getOrElse {
            ResultState.Failure(it)
        }
    }

    private suspend fun getVersionThenStations(): StationsResult {
        Napier.d("getVersionThenStations")
        stationsProxyService.getDataVersion().also {
            Napier.d("version is ${it.first().version}")

            return when (shouldUpdateStations(it.first())) {
                true -> {
                    Napier.d("update stations")
                    saveVersion(it.first())
                    StationsResult(
                        version = it.first(),
                        stations = stationsProxyService.getAllStations()
                    )
                }

                false -> getVersionFromCache()?.let { stationsVersion ->
                    StationsResult(
                        version = DataVersion(
                            stationsVersion.version,
                            stationsVersion.lastUpdate
                        ),
                        stations = getAllStationsFromCache()
                    )
                } ?: throw IllegalStateException("empty version cache")
            }
        }
    }

    private fun shouldUpdateStations(dataVersion: DataVersion): Boolean {
        return getVersionFromCache()?.let {
            dataVersion.version > it.version
        } ?: true
    }

    override fun saveAllStations(stationsResult: StationsResult) {
        Napier.d("saveAllStations version ${stationsResult.version} count ${stationsResult.stations.size}")
        stationsCache.insertAll(stationsResult.stations)
    }

    override fun saveVersion(dataVersion: DataVersion) {
        Napier.d("saveVersion ${dataVersion.version}")
        stationsCache.insertVersion(dataVersion)
    }

    override fun getModelFromCache(stationName: String?, crsCode: String?): StationModel {
        TODO("Not yet implemented")
    }

    override fun getAllStationsFromCache(): List<StationModel> {
        Napier.d("getAllStationsFromCache")
        return stationsCache.getAllStations() ?: throw IllegalStateException("empty stations cache")
    }

    override fun getVersionFromCache(): StationsVersion? {
        return stationsCache.getVersion()
    }
}