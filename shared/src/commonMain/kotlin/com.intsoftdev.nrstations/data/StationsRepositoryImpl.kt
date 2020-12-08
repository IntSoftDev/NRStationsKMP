package com.intsoftdev.nrstations.data

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.DataUpdateAction
import com.intsoftdev.nrstations.cache.DataUpdateResolver
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsResult
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
                when (dataUpdateResolver.getUpdateAction()) {
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
            return StationsResult(
                version = it.first(),
                stations = stationsProxyService.getAllStations()
            )
        }
    }

    override fun saveAllStations(stationsResult: StationsResult) {
        stationsCache.insertAll(stationsResult.stations)
    }

    override fun getModelFromCache(stationName: String?, crsCode: String?): StationModel {
        TODO("Not yet implemented")
    }

    override fun getAllStationsFromCache(): List<StationModel> {
        Napier.d("getAllStationsFromCache")
        return stationsCache.getAllStations()
    }
}