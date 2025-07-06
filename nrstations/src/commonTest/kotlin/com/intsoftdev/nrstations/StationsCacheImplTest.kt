package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.cache.StationsCacheImpl.Companion.DB_TIMESTAMP_KEY
import com.intsoftdev.nrstations.mock.ClockMock
import com.russhwolf.settings.Settings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get

class StationsCacheImplTest : KoinTest {
    private lateinit var dbWrapper: DBWrapper
    private lateinit var settings: Settings
    private lateinit var clockMock: Clock

    private lateinit var sut: StationsCacheImpl

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
        dbWrapper = get()
        settings = get()
        clockMock = get()

        sut =
            StationsCacheImpl(
                dbWrapper = dbWrapper,
                settings = settings,
                clock = clockMock
            )
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `Given stations are inserted when queried then returns expected`() {
        // Given
        sut.insertStations(TEST_STATIONS)

        // When
        val dbStations = dbWrapper.getStations()

        // Then
        dbStations.forEach { stationDb ->
            assertEquals(1, TEST_STATIONS.filter { it.crsCode == stationDb.crs }.size)
        }
    }

    @Test
    fun `Given version is inserted when queried then returns expected`() {
        // Given
        sut.insertVersion(DATA_VERSION_DEFAULT)

        // When
        val stationsVersion = dbWrapper.getVersion()

        // Then
        assertEquals(stationsVersion?.version, DATA_VERSION_DEFAULT.version)
    }

    @Test
    fun `Given stations are inserted when location queried then returns expected`() {
        // Given
        sut.insertStations(listOf(WATERLOO, GATWICK_AIRPORT))

        // When
        val location = dbWrapper.getStationLocation(WATERLOO.crsCode)

        // Then
        assertEquals(location?.crs, WATERLOO.crsCode)
    }

    @Test
    fun `Given cache with data when retrieved then Usable`() {
        // Given
        sut.insertStations(TEST_STATIONS)

        // When
        val cacheState = sut.getCacheState()

        // Then
        assertEquals(cacheState, CacheState.Usable)
    }

    @Test
    fun `Given cache with data when expired then Stale`() {
        // Given
        sut.insertStations(TEST_STATIONS)

        // expire the cache
        val clock = ClockMock(Clock.System.now())
        settings.putLong(
            DB_TIMESTAMP_KEY,
            (clock.currentInstant - 22.hours).toEpochMilliseconds()
        )

        // When
        val cacheState = sut.getCacheState()

        // Then
        assertEquals(cacheState, CacheState.Stale)
    }

    @Test
    fun `Given cache with data when expired and always use then Usable`() {
        // Given
        sut.insertStations(TEST_STATIONS)

        // expire the cache
        val settings = get<Settings>()
        val clock = ClockMock(Clock.System.now())
        settings.putLong(
            DB_TIMESTAMP_KEY,
            (clock.currentInstant - 22.hours).toEpochMilliseconds()
        )

        // When
        val cacheState = sut.getCacheState(cachePolicy = CachePolicy.ALWAYS_USE_CACHE)

        // Then
        assertEquals(cacheState, CacheState.Usable)
    }

    @Test
    fun `Given cache with data when new version available then cache Stale`() {
        // Given
        sut.insertVersion(DATA_VERSION_DEFAULT)
        sut.insertStations(TEST_STATIONS)

        // When
        val cacheState = sut.getCacheState(serverVersion = DATA_VERSION_UPDATE.version)

        // Then
        assertEquals(cacheState, CacheState.Stale)
    }
}
