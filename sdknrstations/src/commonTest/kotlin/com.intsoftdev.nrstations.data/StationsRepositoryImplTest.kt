package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.common.UpdateVersion
import com.intsoftdev.nrstations.data.mock.MockStationsCacheImpl
import com.intsoftdev.nrstations.data.mock.StationsProxyMock
import com.intsoftdev.nrstations.shared.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class StationsRepositoryImplTest : BaseTest() {

    private lateinit var cut: StationsRepositoryImpl
    private val mockStationsCache = MockStationsCacheImpl()
    private val dispatcher = Dispatchers.Main
    private val mockApi = StationsProxyMock()

    @BeforeTest
    fun setup() = runTest {
        cut = StationsRepositoryImpl(
            stationsProxyService = mockApi,
            stationsCache = mockStationsCache,
            requestDispatcher = dispatcher
        )
    }

    @Test
    fun getStationsFromServerNewVersionTest() = runTest {

        // given server refresh
        mockStationsCache.mock.getCacheState.returns(CacheState.Stale)
        // server data version is higher than cached version
        mockApi.mock.getDataVersion.returns(mockApi.stationsVersion())
        mockStationsCache.mock.getVersion.returns(CACHED_VERSION_OLD)

        mockStationsCache.mock.insertVersion.returns(Unit)
        mockStationsCache.mock.insertStations.returns(Unit)

        mockApi.mock.getAllStations.returns(mockApi.stationsData())

        // when get stations
        cut.getAllStations()

        // api calls
        assertTrue(mockApi.mock.getDataVersion.calledCount == 1)
        assertTrue(mockApi.mock.getAllStations.calledCount == 1)

        // cache update calls
        assertTrue(mockStationsCache.mock.insertStations.calledCount == 1)
        assertTrue(mockStationsCache.mock.insertVersion.calledCount == 1)
    }

    @Test
    fun getStationsFromServerSameVersionTest() = runTest {

        // given server refresh
        mockStationsCache.mock.getCacheState.returns(CacheState.Stale)

        // server and cached versions are the same
        mockApi.mock.getDataVersion.returns(mockApi.stationsVersion())
        mockStationsCache.mock.getVersion.returns(CACHED_VERSION_CURRENT)

        cut.getAllStations()

        // api calls
        assertTrue(mockApi.mock.getDataVersion.calledCount == 1)
        // and stations data not refreshed
        assertTrue(mockApi.mock.getAllStations.calledCount == 0)
    }

    @Test
    fun getStationsFromCacheTest() = runTest {
        // given local update
        mockStationsCache.mock.getCacheState.returns(CacheState.Usable)
        mockStationsCache.mock.getVersion.returns(CACHED_VERSION_CURRENT)

        mockStationsCache.mock.getAllStations.returns(mockApi.stationsLocations())

        // when get all stations
        cut.getAllStations()

        // then cached stations and version read
        assertTrue(mockStationsCache.mock.getCacheState.calledCount == 1)
        assertTrue(mockStationsCache.mock.getAllStations.calledCount == 1)
        assertTrue(mockStationsCache.mock.getVersion.calledCount == 1)
        // and api not called
        assertTrue(mockApi.mock.getAllStations.calledCount == 0)
    }

    companion object {
        private val CACHED_VERSION_OLD =
            UpdateVersion(version = 0.9, lastUpdated = 0L)
        private val CACHED_VERSION_CURRENT =
            UpdateVersion(version = 1.0, lastUpdated = 0L)
    }
}