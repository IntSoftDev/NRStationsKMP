package com.intsoftdev.nrstations.data

import com.intsoftdev.nrstations.common.APIConfig
import com.intsoftdev.nrstations.data.model.station.DataVersion
import com.intsoftdev.nrstations.data.model.station.StationModel
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.utils.io.InternalAPI

@OptIn(InternalAPI::class)
internal class StationsProxy(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val azureSubscriptionKey: String? = null
) : StationsAPI {
    private val stationsUrl = "$baseUrl/stations"
    private val versionUrl = "$baseUrl/config/stationsversion.json"


    override suspend fun getAllStations(): List<StationModel> {
        val response =
            httpClient.get(stationsUrl) {
                this.appendHeaders()
            }
        Napier.d("getAllStations totalBytesRead: ${response.content.totalBytesRead}")
        return response.body()
    }

    override suspend fun getDataVersion(): List<DataVersion> {
        val response =
            httpClient.get(versionUrl) {
                this.appendHeaders()
            }
        return response.body()
    }

    private fun HttpRequestBuilder.appendHeaders() =
        azureSubscriptionKey?.let {
            this.headers {
                append(APIConfig.SUBSCRIPTION_PROP_KEY, it)
            }
        }
}
