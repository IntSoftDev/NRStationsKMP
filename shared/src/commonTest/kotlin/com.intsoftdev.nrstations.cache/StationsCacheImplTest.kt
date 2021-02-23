package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.StationsCacheImpl.Companion.DB_TIMESTAMP_KEY
import com.intsoftdev.nrstations.cache.entities.VersionEntity
import com.intsoftdev.nrstations.cache.mock.ClockMock
import com.intsoftdev.nrstations.cache.mock.MockDBWrapperImpl
import com.intsoftdev.nrstations.shared.BaseTest
import com.russhwolf.settings.MockSettings
import kotlinx.datetime.Clock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.hours

class StationsCacheImplTest : BaseTest() {

    private lateinit var cut: StationsCacheImpl
    private val settings = MockSettings()
    private val dbWrapper = MockDBWrapperImpl()
    private val clock = ClockMock(Clock.System.now())

    @BeforeTest
    fun setup() = runTest {
        cut = StationsCacheImpl(dbWrapper = dbWrapper, settings = settings, clock = clock)
    }

    @Test
    fun emptyCacheTest() = runTest {
        dbWrapper.mock.isEmpty.returns(true)
        val cacheState = cut.getCacheState()
        assertEquals(expected = CacheState.Empty, actual = cacheState)
    }

    @Test
    fun cacheUsableTest() = runTest {
        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, currentTimeMS)
        dbWrapper.mock.isEmpty.returns(false)
        val cacheState = cut.getCacheState()
        assertEquals(expected = CacheState.Usable, actual = cacheState)
    }

    @Test
    fun cacheStaleVersionTest() = runTest {
        val versionEntity = VersionEntity(version = 1.0, lastUpdate = 0)
        dbWrapper.mock.isEmpty.returns(false)
        dbWrapper.mock.getVersion.returns(versionEntity)
        val serverVersion = 2.0
        val cacheState = cut.getCacheState(serverVersion)
        assertEquals(expected = CacheState.Stale, actual = cacheState)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun cacheStaleExpiredTest() = runTest {
        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, currentTimeMS)
        dbWrapper.mock.isEmpty.returns(false)
        clock.currentInstant += 13.hours
        val cacheState = cut.getCacheState()
        assertEquals(expected = CacheState.Stale, actual = cacheState)
    }
}