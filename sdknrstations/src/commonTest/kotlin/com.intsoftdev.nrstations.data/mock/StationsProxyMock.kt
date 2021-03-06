package com.intsoftdev.nrstations.data.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel
import com.intsoftdev.nrstations.data.model.station.toStationLocation

internal class StationsProxyMock : StationsAPI {
    // Call recording provided by experimental library Karmok
    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val getAllStations =
            MockFunctionRecorder<StationsProxyMock, List<StationModel>>()
        internal val getDataVersion =
            MockFunctionRecorder<StationsProxyMock, List<DataVersion>>()
    }

    override suspend fun getAllStations(): List<StationModel> {
        return mock.getAllStations.invokeSuspend({ getAllStations() }, listOf())
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        return mock.getDataVersion.invokeSuspend({ getDataVersion() }, listOf())
    }

    fun stationsData(): List<StationModel> = listOf(LONDON_WATERLOO, WHITTON)

    fun stationsLocations() : List<StationLocation> = stationsData().map {
        it.toStationLocation()
    }

    fun stationsVersion(): List<DataVersion> = listOf(STATIONS_SERVER_VERSION)

    companion object {
        private val LONDON_WATERLOO = StationModel("London Waterloo", "WAT", 51.50329, -0.113108)
        private val WHITTON = StationModel("Whitton", "WTN", 51.532388, -0.127189433)
        private val STATIONS_SERVER_VERSION = DataVersion(1.0, 0L)
    }
}