package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.cache.DBWrapperImpl
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.database.NRStationsDb
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class DBWrapperImplTest {

    private lateinit var dbWrapper: DBWrapperImpl

    private val testStation = StationLocation("London Waterloo", "WAT", 0.0, 0.0)

    @BeforeTest
    fun setup() = runTest {
        dbWrapper = DBWrapperImpl(
            NRStationsDb(testDbConnection())
        )
        dbWrapper.insertStations(listOf(testStation))
    }

    @Test
    fun `Select All Items Success`() = runTest {
        val station = dbWrapper.getStations()
        assertNotNull(
            station.find { it.name == "London Waterloo" },
            "Could not retrieve station"
        )
    }

    @Test
    fun `Get Station By Id Success`() = runTest {
        val station = dbWrapper.getStationLocation("WAT")
        assertNotNull(
            station?.latitude,
            "Could not retrieve station"
        )
    }
}