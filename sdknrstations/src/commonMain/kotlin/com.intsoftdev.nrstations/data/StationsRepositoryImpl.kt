package com.intsoftdev.nrstations.data

import io.github.aakira.napier.Napier
import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.toStationLocation
import com.intsoftdev.nrstations.data.model.station.toUpdateVersion
import com.intsoftdev.nrstations.domain.StationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class StationsRepositoryImpl(
    private val stationsProxyService: StationsAPI,
    private val stationsCache: StationsCache,
    private val requestDispatcher: CoroutineDispatcher
) : StationsRepository {

    override fun getAllStations(): Flow<StationsResultState<StationsResult>> =
        flow {
            when (stationsCache.getCacheState()) {
                is CacheState.Empty, CacheState.Stale -> {
                    emit(refreshStations())
                }
                is CacheState.Usable -> {
                    emit(
                        StationsResultState.Success(
                            StationsResult(
                                version = stationsCache.getVersion(),
                                stations = stationsCache.getAllStations()
                            )
                        )
                    )
                }
            }
        }.catch { throwable ->
            emit(StationsResultState.Failure(throwable))
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

                    val version = serverDataVersion.toUpdateVersion()

                    stationsCache.insertStations(stationLocations)
                    stationsCache.insertVersion(version)

                    StationsResultState.Success(
                        StationsResult(
                            version = version,
                            stations = stationLocations
                        )
                    )
                }

                is CacheState.Usable -> {
                    Napier.d("getStationsFromCache")
                    StationsResultState.Success(
                        StationsResult(
                            version = stationsCache.getVersion(),
                            stations = stationsCache.getAllStations()
                        )
                    )
                }
            }
        }
    }
}