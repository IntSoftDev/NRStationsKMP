package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.common.UpdateVersion
import com.intsoftdev.nrstations.data.mock.MockStationsCacheImpl
import com.intsoftdev.nrstations.data.mock.StationsProxyMock
import com.intsoftdev.nrstations.shared.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlin.test.BeforeTest
import kotlin.test.Test

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

    @OptIn(FlowPreview::class)
    @Test
    fun getStationsFromServerNewVersionTest() = runTest {
        // TODO
    }

    @OptIn(FlowPreview::class)
    @Test
    fun getStationsFromServerSameVersionTest() = runTest {
        // TODO
    }

    @OptIn(FlowPreview::class)
    @Test
    fun getStationsFromCacheTest() = runTest {
        // TODO
    }

    companion object {
        private val CACHED_VERSION_OLD =
            UpdateVersion(version = 0.9, lastUpdated = 0L)
        private val CACHED_VERSION_CURRENT =
            UpdateVersion(version = 1.0, lastUpdated = 0L)
    }
}