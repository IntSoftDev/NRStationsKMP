package com.intsoftdev.nrstations.data

import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.toStationLocation
import com.intsoftdev.nrstations.data.model.station.toUpdateVersion
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.sdk.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class StationsRepositoryImpl(
    private val stationsProxyService: StationsAPI,
    private val stationsCache: StationsCache,
    private val requestDispatcher: CoroutineDispatcher
) : StationsRepository {

    override suspend fun getAllStations(): ResultState<StationsResult> {
        return runCatching {
            withContext(requestDispatcher) {
                when (stationsCache.getCacheState()) {
                    is CacheState.Empty, CacheState.Stale -> {
                        ResultState.Success(refreshStations())
                    }
                    is CacheState.Usable -> {
                        ResultState.Success(
                            StationsResult(
                                version = stationsCache.getVersion(),
                                stations = stationsCache.getAllStations()
                            )
                        )
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

    private suspend fun refreshStations(): StationsResult {
        Napier.d("refreshStations enter")
        getServerDataVersion().also { serverDataVersion ->
            return when (stationsCache.getCacheState(serverDataVersion.version)) {
                is CacheState.Empty, CacheState.Stale -> {
                    Napier.d("getStationsFromServer")
                    val stationLocations = stationsProxyService.getAllStations().map {
                        it.toStationLocation()
                    }

                    val version = serverDataVersion.toUpdateVersion()

                    stationsCache.insertStations(stationLocations)
                    stationsCache.insertVersion(version)

                    StationsResult(
                        version = version,
                        stations = stationLocations
                    )
                }

                is CacheState.Usable -> {
                    Napier.d("getStationsFromCache")
                    StationsResult(
                        version = stationsCache.getVersion(),
                        stations = stationsCache.getAllStations()
                    )
                }
            }
        }
    }
}