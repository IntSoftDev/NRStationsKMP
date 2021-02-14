package com.intsoftdev.nrstations.data.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.data.StationsServiceAPI
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsVersion

class StationsProxyServiceMock : StationsServiceAPI {
    // Call recording provided by experimental library Karmok
    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val getAllStations =
            MockFunctionRecorder<StationsProxyServiceMock, List<StationModel>>()
        internal val getDataVersion =
            MockFunctionRecorder<StationsProxyServiceMock, List<DataVersion>>()
    }

    override suspend fun getAllStations(): List<StationModel> {
        return mock.getAllStations.invokeSuspend({ getAllStations() }, listOf())
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        return mock.getDataVersion.invokeSuspend({ getDataVersion() }, listOf())
    }

    fun stationsResult(): List<StationModel> = listOf(LONDON_WATERLOO, WHITTON)

    fun stationsVersion(): List<DataVersion> = listOf(STATIONS_SERVER_VERSION)

    companion object {
        private val LONDON_WATERLOO = StationModel("id1", "London Waterloo", "WAT", 51.50329, -0.113108)
        private val WHITTON = StationModel("id2", "Whitton", "WTN", 51.532388, -0.127189433)
        private val STATIONS_SERVER_VERSION = DataVersion(1.0, 0L)
    }
}