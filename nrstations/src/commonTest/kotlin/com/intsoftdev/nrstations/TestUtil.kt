package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.datetime.Clock

internal expect fun testDbConnection(): SqlDriver

internal val WATERLOO = StationLocation("London Waterloo", "WAT", 51.50329, -0.113108)
internal val GATWICK_AIRPORT = StationLocation("Gatwick Airport", "GAT", 51.1564, -0.16104152)
internal val TEST_STATIONS = listOf(WATERLOO, GATWICK_AIRPORT)

internal val STATION_MODEL_WATERLOO = StationModel("London Waterloo", "WAT", 51.50329, -0.113108)
internal val STATION_MODEL_GATWICK = StationModel("Gatwick Airport", "GAT", 51.1564, -0.16104152)
internal val TEST_STATIONS_API_RESPONSE = listOf(STATION_MODEL_WATERLOO, STATION_MODEL_GATWICK)

internal val DATA_VERSION_DEFAULT = DataVersion(0.1, Clock.System.now().epochSeconds)
internal val DATA_VERSION_UPDATE = DataVersion(0.2, Clock.System.now().epochSeconds)
