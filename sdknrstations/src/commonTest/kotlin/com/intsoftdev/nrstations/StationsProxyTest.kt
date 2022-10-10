package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.data.StationsProxy
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StationsProxyTest {

    val jsonResponse =
        "[{\"latitude\":51.491060763749196,\"longitude\":0.12139402996250627,\"stationName\":\"Abbey Wood (London)\",\"crsCode\":\"ABW\"}]"

    @Test
    fun success() = runTest {
        val engine = MockEngine {
            assertEquals("https://onrails.azurewebsites.net/stations", it.url.toString())
            respond(
                content = jsonResponse,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val httpClient = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }

        val stationsApi = StationsProxy(httpClient)

        val result = stationsApi.getAllStations()

        assertEquals(
            "ABW",
            result.first().crsCode
        )
    }

    @Test
    fun failure() = runTest {
        val engine = MockEngine {
            assertEquals("https://onrails.azurewebsites.net/stations", it.url.toString())
            respond(
                content = "",
                status = HttpStatusCode.NotFound
            )
        }

        val httpClient = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }

        val stationsApi = StationsProxy(httpClient)

        assertFailsWith<ClientRequestException> {
            stationsApi.getAllStations()
        }
    }
}
