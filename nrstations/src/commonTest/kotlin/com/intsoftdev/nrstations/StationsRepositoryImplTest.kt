package com.intsoftdev.nrstations

import app.cash.turbine.test // ktlint-disable import-ordering
import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.common.DefaultRetryPolicy
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.mock.ClockMock
import com.intsoftdev.nrstations.mock.StationsApiMock
import com.russhwolf.settings.Settings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get

class StationsRepositoryImplTest : KoinTest {

    private val mockApi = StationsApiMock()

    private val testRetryPolicy = DefaultRetryPolicy(numRetries = 0)

    private lateinit var sut: StationsRepositoryImpl

    private lateinit var stationsCache: StationsCache
    private lateinit var settings: Settings

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }

        stationsCache = get()
        settings = get()

        sut = StationsRepositoryImpl(
            stationsProxyService = mockApi,
            stationsCache = stationsCache,
            requestDispatcher = Dispatchers.Default,
            requestRetryPolicy = testRetryPolicy
        )
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `Given success API response when get all stations then result expected`() = runTest {
        // Given
        mockApi.prepareStationsResponse(mockApi.stationsSuccessResponse())
        mockApi.prepareDataVersionResponse(mockApi.dataVersionSuccessResponse())

        // When
        sut.getAllStations(cachePolicy = CachePolicy.FORCE_REFRESH).test {
            val stationsResult = awaitItem()
            assertIs<StationsResultState.Success<StationsResult>>(stationsResult)
            stationsResult.data.stations.forEach { station ->
                assertEquals(1, TEST_STATIONS.filter { it.crsCode == station.crsCode }.size)
            }
            awaitComplete()
        }

        // Then
        stationsCache.getAllStations().forEach { station ->
            assertEquals(1, TEST_STATIONS.filter { it.crsCode == station.crsCode }.size)
        }
        assertEquals(DATA_VERSION_UPDATE.version, stationsCache.getVersion().version)
    }

    @Test
    fun `Given stations cache exists when API request then no API calls are made`() = runTest {
        // Given
        mockApi.prepareStationsResponse(mockApi.stationsSuccessResponse())
        mockApi.prepareDataVersionResponse(mockApi.dataVersionSuccessResponse())
        stationsCache.insertStations(TEST_STATIONS)
        stationsCache.insertVersion(DATA_VERSION_DEFAULT)

        // When
        sut.getAllStations(cachePolicy = CachePolicy.USE_CACHE_WITH_EXPIRY).test {
            assertTrue { awaitItem() is StationsResultState.Success }
            awaitComplete()
        }

        // Then
        assertEquals(0, mockApi.calledCount)
    }

    @Test
    fun `Given cache expires when get all stations then API called and cache updated`() = runTest {
        // Given
        mockApi.prepareStationsResponse(mockApi.stationsSuccessResponse())
        mockApi.prepareDataVersionResponse(mockApi.dataVersionSuccessResponse())

        stationsCache.insertStations(TEST_STATIONS)
        stationsCache.insertVersion(DATA_VERSION_DEFAULT)

        val clock = ClockMock(Clock.System.now())
        settings.putLong(
            StationsCacheImpl.DB_TIMESTAMP_KEY,
            (clock.currentInstant - 22.hours).toEpochMilliseconds()
        )

        // When
        sut.getAllStations(cachePolicy = CachePolicy.USE_CACHE_WITH_EXPIRY).test {
            assertTrue { awaitItem() is StationsResultState.Success }
            awaitComplete()
        }

        // Then
        assertEquals(2, mockApi.calledCount)
        assertEquals(DATA_VERSION_UPDATE.version, stationsCache.getVersion().version)
    }

    @Test
    fun `When API response returns error then result expected`() = runTest {
        // Given
        val exception = RuntimeException("unknown error")
        mockApi.throwOnCall(exception)
        mockApi.prepareDataVersionResponse(mockApi.dataVersionSuccessResponse())

        // When
        sut.getAllStations(cachePolicy = CachePolicy.FORCE_REFRESH).test {
            val response = awaitItem()
            // Then
            assertIs<StationsResultState.Failure>(response)
            assertEquals(exception, response.error)
            awaitComplete()
        }
    }
}
