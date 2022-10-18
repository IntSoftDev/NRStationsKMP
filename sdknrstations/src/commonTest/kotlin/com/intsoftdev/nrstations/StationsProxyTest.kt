package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.data.StationsProxy
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get

class StationsProxyTest : KoinTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun success() = runTest {
        val httpClientSuccess = get<HttpClient>(named("HttpTestClientSuccess"))

        val stationsApi = StationsProxy(httpClientSuccess, MOCK_SERVER_BASE_URL)

        val result = stationsApi.getAllStations()

        assertEquals(
            "ABW",
            result.first().crsCode
        )
    }

    @Test
    fun failure() = runTest {
        val httpClientFailure = get<HttpClient>(named("HttpTestClientError"))

        val stationsApi = StationsProxy(httpClientFailure, MOCK_SERVER_BASE_URL)

        assertFailsWith<ClientRequestException> {
            stationsApi.getAllStations()
        }
    }

    companion object {
        internal const val MOCK_SERVER_BASE_URL = "http://example.com"
        internal const val MOCK_SERVER_STATIONS_URL = "http://example.com/stations"
    }
}
