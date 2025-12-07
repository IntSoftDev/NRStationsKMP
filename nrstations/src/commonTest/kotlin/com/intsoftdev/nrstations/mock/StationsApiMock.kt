package com.intsoftdev.nrstations.mock

import com.intsoftdev.nrstations.DATA_VERSION_UPDATE
import com.intsoftdev.nrstations.TEST_STATIONS_API_RESPONSE
import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel

internal class StationsApiMock : StationsAPI {
    var calledCount = 0
        private set

    private var nextStationsResponse: () -> List<StationModel> = { error("Uninitialized!") }

    private var nextDataVersionResponse: () -> List<DataVersion> = { error("Uninitialized!") }

    override suspend fun getAllStations(): List<StationModel> {
        val result = nextStationsResponse()
        calledCount++
        return result
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        val result = nextDataVersionResponse()
        calledCount++
        return result
    }

    fun prepareStationsResponse(stations: List<StationModel>) {
        nextStationsResponse = { stations }
    }

    fun stationsSuccessResponse(): List<StationModel> = TEST_STATIONS_API_RESPONSE

    fun prepareDataVersionResponse(dataVersion: List<DataVersion>) {
        nextDataVersionResponse = { dataVersion }
    }

    fun dataVersionSuccessResponse(): List<DataVersion> = listOf(DATA_VERSION_UPDATE)

    fun throwOnCall(throwable: Throwable) {
        nextStationsResponse = { throw throwable }
    }
}
