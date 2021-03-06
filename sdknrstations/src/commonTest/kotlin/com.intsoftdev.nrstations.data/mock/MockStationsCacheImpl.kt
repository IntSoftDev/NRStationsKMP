package com.intsoftdev.nrstations.data.mock

import co.touchlab.karmok.MockManager
import com.intsoftdev.nrstations.cache.CacheState
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.UpdateVersion

internal class MockStationsCacheImpl : StationsCache {
    internal val mock = InnerMock()

    class InnerMock(delegate: Any? = null) : MockManager(delegate) {
        internal val insertStations = MockFunctionRecorder<MockStationsCacheImpl, Unit>()
        internal val insertVersion = MockFunctionRecorder<MockStationsCacheImpl, Unit>()
        internal val getAllStations =
            MockFunctionRecorder<MockStationsCacheImpl, List<StationLocation>>()
        internal val getCacheState = MockFunctionRecorder<MockStationsCacheImpl, CacheState>()
        internal val getVersion = MockFunctionRecorder<MockStationsCacheImpl, UpdateVersion>()
    }

    override fun insertStations(stations: List<StationLocation>) {
        return mock.insertStations.invoke { insertStations(listOf()) }
    }

    override fun insertVersion(version: UpdateVersion) {
        return mock.insertVersion.invoke { insertVersion(version) }
    }

    override fun getAllStations(): List<StationLocation> {
        return mock.getAllStations.invoke({ getAllStations() }, listOf())
    }

    override fun getCacheState(serverVersion: Double?): CacheState {
        return mock.getCacheState.invoke({ getCacheState(serverVersion) }, listOf())
    }

    override fun getVersion(): UpdateVersion {
        return mock.getVersion.invoke({ getVersion() }, listOf())
    }
}