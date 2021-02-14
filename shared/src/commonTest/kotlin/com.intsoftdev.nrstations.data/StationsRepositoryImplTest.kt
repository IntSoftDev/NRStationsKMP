package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.cache.DataUpdateAction
import com.intsoftdev.nrstations.data.mock.MockStationsCacheImpl
import com.intsoftdev.nrstations.data.mock.StationsProxyServiceMock
import com.intsoftdev.nrstations.model.StationsVersion
import com.intsoftdev.nrstations.shared.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class StationsRepositoryImplTest : BaseTest() {

    private lateinit var cut: StationsRepositoryImpl
    private val mockStationsCache = MockStationsCacheImpl()
    private val dispatcher = Dispatchers.Main
    private val mockApi = StationsProxyServiceMock()

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
        mockStationsCache.mock.getUpdateAction.returns(DataUpdateAction.REFRESH)

        // server data version is higher than cached version
        mockApi.mock.getDataVersion.returns(mockApi.stationsVersion())
        mockStationsCache.mock.getVersion.returns(CACHED_VERSION_OLD)

        mockStationsCache.mock.insertVersion.returns(Unit)
        mockStationsCache.mock.insertStations.returns(Unit)

        mockApi.mock.getAllStations.returns(mockApi.stationsResult())

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
        mockStationsCache.mock.getUpdateAction.returns(DataUpdateAction.REFRESH)

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
        mockStationsCache.mock.getUpdateAction.returns(DataUpdateAction.LOCAL)
        mockStationsCache.mock.getVersion.returns(CACHED_VERSION_CURRENT)

        mockStationsCache.mock.getAllStations.returns(mockApi.stationsResult())

        // when get all stations
        cut.getAllStations()

        // then cached stations and version read
        assertTrue(mockStationsCache.mock.getUpdateAction.calledCount == 1)
        assertTrue(mockStationsCache.mock.getAllStations.calledCount == 1)
        assertTrue(mockStationsCache.mock.getVersion.calledCount == 1)
        // and api not called
        assertTrue(mockApi.mock.getAllStations.calledCount == 0)
    }

    companion object {
        private val CACHED_VERSION_OLD =
            StationsVersion("stationsVersion", version = 0.9, lastUpdate = 0L)
        private val CACHED_VERSION_CURRENT =
            StationsVersion("stationsVersion", version = 1.0, lastUpdate = 0L)
    }
}