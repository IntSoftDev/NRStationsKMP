package com.intsoftdev.nrstations.cache.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.model.StationsList
import com.intsoftdev.nrstations.model.StationsVersion

class MockDBWrapperImpl : DBWrapper {

    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val insertStations = MockFunctionRecorder<MockDBWrapperImpl, Unit>()
        internal val getStations = MockFunctionRecorder<MockDBWrapperImpl, StationsList?>()
        internal val insertVersion =
            MockFunctionRecorder<MockDBWrapperImpl, Unit>()
        internal val getVersion = MockFunctionRecorder<MockDBWrapperImpl, StationsVersion?>()
        internal val isEmpty =
            MockFunctionRecorder<MockDBWrapperImpl, Boolean>()
    }

    override fun insertStations(stations: StationsList) {
        return mock.insertStations.invoke { insertStations(stations) }
    }

    override fun getStations(): StationsList? {
        return mock.getStations.invoke({ getStations() }, listOf())
    }

    override fun insertVersion(version: StationsVersion) {
        return mock.insertVersion.invoke {
            insertVersion(
                version
            )
        }
    }

    override fun getVersion(): StationsVersion? {
        return mock.getVersion.invoke({ getVersion() }, listOf())
    }

    override fun isEmpty(): Boolean {
        return mock.isEmpty.invoke({ isEmpty() }, listOf())
    }
}