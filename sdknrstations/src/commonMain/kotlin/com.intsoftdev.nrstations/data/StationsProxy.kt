package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class StationsProxy(private val httpClient: HttpClient) : StationsAPI {

    private val baseUrl = "https://onrails.azurewebsites.net/stations"
    private val versionUrl = "https://onrails.azurewebsites.net/config/stationsversion.json"

    override suspend fun getAllStations(): List<StationModel> {
        val response = httpClient.get(baseUrl)
        return response.body()
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        val response = httpClient.get(versionUrl)
        return response.body()
    }
}
