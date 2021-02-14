package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.model.DataVersion
import com.intsoftdev.nrstations.model.StationModel
import io.ktor.client.*
import io.ktor.client.request.*

internal class StationsProxyService(private val httpClient: HttpClient) : StationsServiceAPI {

    private val baseUrl = "https://onrails.azurewebsites.net/stations"
    private val versionUrl = "https://onrails.azurewebsites.net/config/stationsversion.json"

    override suspend fun getAllStations(): List<StationModel> {
        return httpClient.get(baseUrl)
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        return httpClient.get(versionUrl)
    }
}