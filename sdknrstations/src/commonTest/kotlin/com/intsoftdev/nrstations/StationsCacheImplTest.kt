package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.common.StationLocation
import com.russhwolf.settings.Settings
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StationsCacheImplTest : KoinTest {

    private val testStation = StationLocation("London Waterloo", "WAT", 0.0, 0.0)

    private val stationsCache : StationsCache by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun success() = runTest {
        stationsCache.insertStations(listOf(testStation))

        val dbWrapper = get<DBWrapper>()
        val settings = get<Settings>()
        val clock = get<Clock>()

        val first = dbWrapper.getStations().first()

        println("first is $first")

        val updateTime = settings.getLong(StationsCacheImpl.DB_TIMESTAMP_KEY, 0L)
        assertEquals(clock.now().toEpochMilliseconds(), updateTime)
    }
}