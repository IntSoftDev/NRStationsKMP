package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.common.Geolocation
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.RequestRetryPolicy
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.common.retryWithPolicy
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.toStationLocation
import com.intsoftdev.nrstations.data.model.station.toUpdateVersion
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.location.getSortedStations
import com.intsoftdev.nrstations.location.getStationDistancesfromRefPoint
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

    override suspend fun getStationLocation(vararg crsCodes: String): List<StationLocation> =
        when (stationsCache.getCacheState()) {
            is CacheState.Empty -> {
                throw IllegalStateException("cache empty")
            }
            else -> {
                crsCodes.map {
                    stationsCache.getStationLocation(it)
                }
            }
        }

    override fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): Flow<StationsResultState<NearestStations>> =
        flow {
            when (val cacheState = stationsCache.getCacheState()) {
                is CacheState.Empty -> {
                    Napier.d("cacheState is $cacheState")
                    with(refreshStations()) {
                        when (this) {
                            is StationsResultState.Success -> {
                                val stations = sortStationsFromCache(latitude, longitude)
                                Napier.d("getNearbyStations Success")
                                emit(StationsResultState.Success(stations))
                            }
                            is StationsResultState.Failure -> {
                                emit(
                                    StationsResultState.Failure(
                                        IllegalStateException("cache empty")
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    val stations = sortStationsFromCache(latitude, longitude)
                    Napier.d("getNearbyStations Success")
                    emit(StationsResultState.Success(stations))
                }
            }
        }.flowOn(requestDispatcher)

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

    private fun sortStationsFromCache(
        latitude: Double,
        longitude: Double
    ): NearestStations {
        val sortedStations = getSortedStations(
            latitude,
            longitude,
            stationsCache.getAllStations()
        ).subList(
            0,
            MAX_NEARBY_STATIONS - 1
        )
        return getStationDistancesfromRefPoint(
            Geolocation(latitude, longitude),
            sortedStations
        )
    }

    companion object {
        private const val MAX_NEARBY_STATIONS = 10
    }
}