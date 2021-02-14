package com.intsoftdev.nrstations.cache

import com.intsoftdev.nrstations.cache.StationsCacheImpl.Companion.DB_TIMESTAMP_KEY
import com.intsoftdev.nrstations.cache.mock.ClockMock
import com.intsoftdev.nrstations.cache.mock.MockDBWrapperImpl
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.model.StationsVersion
import com.intsoftdev.nrstations.shared.BaseTest
import com.russhwolf.settings.MockSettings
import kotlinx.datetime.Clock
import kotlin.test.*

import kotlin.time.ExperimentalTime
import kotlin.time.hours

class StationsCacheImplTest : BaseTest(){

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
        val action = cut.getUpdateAction()
        assertEquals(expected = DataUpdateAction.REFRESH, actual = action)
    }

    @Test
    fun cacheNotStateTest() = runTest {
        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, currentTimeMS)
        dbWrapper.mock.isEmpty.returns(false)
        val action = cut.getUpdateAction()
        assertEquals(expected = DataUpdateAction.LOCAL, actual = action)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun cacheStateTest() = runTest {
        val currentTimeMS = Clock.System.now().toEpochMilliseconds()
        settings.putLong(DB_TIMESTAMP_KEY, currentTimeMS)
        dbWrapper.mock.isEmpty.returns(false)
        clock.currentInstant += 13.hours
        val action = cut.getUpdateAction()
        assertEquals(expected = DataUpdateAction.REFRESH, actual = action)
    }
}