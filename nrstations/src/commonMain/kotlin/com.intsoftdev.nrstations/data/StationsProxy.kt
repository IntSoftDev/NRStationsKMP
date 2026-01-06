package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class StationsProxy(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : StationsAPI {
    private val stationsUrl = "$baseUrl/stations"
    private val versionUrl = "$baseUrl/config/stationsversion.json"

    override suspend fun getAllStations(): List<StationModel> {
        val response = httpClient.get(stationsUrl)
        return response.body()
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        val response = httpClient.get(versionUrl)
        return response.body()
    }
}
