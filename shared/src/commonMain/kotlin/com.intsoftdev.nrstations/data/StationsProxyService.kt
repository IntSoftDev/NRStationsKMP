package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.model.StationModel
import io.ktor.client.*
import io.ktor.client.request.*

class StationsProxyService(private val httpClient: HttpClient) : StationsServiceAPI {

    private val baseUrl = "https://onrails.azurewebsites.net/stations"

    override suspend fun getAllStations(): List<StationModel> {
        return httpClient.get(baseUrl)
    }
}