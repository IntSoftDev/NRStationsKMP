package com.intsoftdev.nrstations.cache.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.cache.entities.StationEntity
import com.intsoftdev.nrstations.cache.entities.StationsEntity
import com.intsoftdev.nrstations.cache.entities.VersionEntity

internal class MockDBWrapperImpl : DBWrapper {

    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val insertStations = MockFunctionRecorder<MockDBWrapperImpl, Unit>()
        internal val getStations = MockFunctionRecorder<MockDBWrapperImpl, StationsEntity?>()
        internal val getLocation = MockFunctionRecorder<MockDBWrapperImpl, StationEntity?>()
        internal val insertVersion =
            MockFunctionRecorder<MockDBWrapperImpl, Unit>()
        internal val getVersion = MockFunctionRecorder<MockDBWrapperImpl, VersionEntity?>()
        internal val isEmpty =
            MockFunctionRecorder<MockDBWrapperImpl, Boolean>()
    }

    override fun insertStations(stations: StationsEntity) {
        return mock.insertStations.invoke { insertStations(stations) }
    }

    override fun getStations(): StationsEntity? {
        return mock.getStations.invoke({ getStations() }, listOf())
    }

    override fun getStationLocation(stationId: String): StationEntity? {
        return mock.getLocation.invoke({ getStationLocation(stationId) }, listOf())
    }

    override fun insertVersion(version: VersionEntity) {
        return mock.insertVersion.invoke {
            insertVersion(
                version
            )
        }
    }

    override fun getVersion(): VersionEntity? {
        return mock.getVersion.invoke({ getVersion() }, listOf())
    }

    override fun isEmpty(): Boolean {
        return mock.isEmpty.invoke({ isEmpty() }, listOf())
    }
}