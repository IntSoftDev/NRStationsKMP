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
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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

        val stationsApi = StationsProxy(httpClientSuccess)

        val result = stationsApi.getAllStations()

        assertEquals(
            "ABW",
            result.first().crsCode
        )
    }

    @Test
    fun failure() = runTest {

        val httpClientFailure = get<HttpClient>(named("HttpTestClientError"))

        val stationsApi = StationsProxy(httpClientFailure)

        assertFailsWith<ClientRequestException> {
            stationsApi.getAllStations()
        }
    }
}
