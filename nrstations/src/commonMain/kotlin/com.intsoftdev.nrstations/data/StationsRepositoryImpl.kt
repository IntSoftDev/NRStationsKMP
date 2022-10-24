package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.common.RequestRetryPolicy
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.common.retryWithPolicy
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.toStationLocation
import com.intsoftdev.nrstations.data.model.station.toUpdateVersion
import com.intsoftdev.nrstations.domain.StationsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class StationsRepositoryImpl(
    private val stationsProxyService: StationsAPI,
    private val stationsCache: StationsCache,
    private val requestDispatcher: CoroutineDispatcher,
    private val requestRetryPolicy: RequestRetryPolicy
) : StationsRepository {

    override fun getAllStations(): Flow<StationsResultState<StationsResult>> =
        flow {
            when (val cacheState = stationsCache.getCacheState()) {
                is CacheState.Empty, CacheState.Stale -> {
                    Napier.d("cacheState is $cacheState")
                    emit(refreshStations())
                }
                is CacheState.Usable -> {
                    Napier.d("cacheState is $cacheState")
                    val result = StationsResult(
                        version = stationsCache.getVersion().toUpdateVersion(),
                        stations = stationsCache.getAllStations()
                    )
                    Napier.d("cached stations ${result.stations.size} version ${result.version.version}")
                    emit(StationsResultState.Success(result))
                }
            }
        }.retryWithPolicy(requestRetryPolicy).catch { throwable ->
            emit(StationsResultState.Failure(throwable))
        }.flowOn(requestDispatcher)

    override fun getStationLocation(crsCode: String?): StationLocation {
        requireNotNull(crsCode)
        return when (stationsCache.getCacheState()) {
            is CacheState.Empty -> {
                throw IllegalStateException("cache empty")
            }
            else -> {
                stationsCache.getStationLocation(crsCode)
            }
        }
    }

    private suspend fun getServerDataVersion(): DataVersion {
        return stationsProxyService.getDataVersion().first()
    }

    private suspend fun refreshStations(): StationsResultState<StationsResult> {
        Napier.d("refreshStations enter")
        getServerDataVersion().also { serverDataVersion ->
            return when (stationsCache.getCacheState(serverDataVersion.version)) {
                is CacheState.Empty, CacheState.Stale -> {
                    Napier.d("getStationsFromServer")
                    val stationLocations = stationsProxyService.getAllStations().map {
                        it.toStationLocation()
                    }

                    stationsCache.insertStations(stationLocations)
                    stationsCache.insertVersion(serverDataVersion)

                    Napier.d("got stations ${stationLocations.size} version ${serverDataVersion.version}")

                    StationsResultState.Success(
                        StationsResult(
                            version = serverDataVersion.toUpdateVersion(),
                            stations = stationLocations
                        )
                    )
                }

                is CacheState.Usable -> {
                    Napier.d("getStationsFromCache")
                    val result = StationsResult(
                        version = stationsCache.getVersion().toUpdateVersion(),
                        stations = stationsCache.getAllStations()
                    )
                    Napier.d("read stations ${result.stations.size} version ${result.version.version}")
                    StationsResultState.Success(result)
                }
            }
        }
    }
}
