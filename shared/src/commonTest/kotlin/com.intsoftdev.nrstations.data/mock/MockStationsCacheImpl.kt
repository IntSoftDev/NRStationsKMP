package com.intsoftdev.nrstations.data.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.cache.DataUpdateAction
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import com.intsoftdev.nrstations.model.StationsVersion

internal class MockStationsCacheImpl : StationsCache {
    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val insertStations = MockFunctionRecorder<MockStationsCacheImpl, Unit>()
        internal val insertVersion = MockFunctionRecorder<MockStationsCacheImpl, Unit>()
        internal val getAllStations =
            MockFunctionRecorder<MockStationsCacheImpl, List<StationModel>?>()
        internal val isCacheEmpty = MockFunctionRecorder<MockStationsCacheImpl, Boolean>()
        internal val getVersion = MockFunctionRecorder<MockStationsCacheImpl, StationsVersion?>()
        internal val getUpdateAction =
            MockFunctionRecorder<MockStationsCacheImpl, DataUpdateAction>()
    }

    override fun insertStations(stations: List<StationModel>) {
        return mock.insertStations.invoke { insertStations(listOf()) }
    }

    override fun insertVersion(version: DataVersion) {
        return mock.insertVersion.invoke { insertVersion(version) }
    }

    override fun getAllStations(): List<StationModel>? {
        return mock.getAllStations.invoke({ getAllStations() }, listOf())
    }

    override fun isCacheEmpty(): Boolean {
        return mock.isCacheEmpty.invoke({ isCacheEmpty() }, listOf())
    }

    override fun getVersion(): StationsVersion? {
        return mock.getVersion.invoke({ getVersion() }, listOf())
    }

    override fun getUpdateAction(): DataUpdateAction {
        return mock.getUpdateAction.invoke({ getUpdateAction() }, listOf())
    }
}