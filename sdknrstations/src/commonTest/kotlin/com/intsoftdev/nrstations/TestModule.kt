package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.cache.DBWrapper // ktlint-disable import-ordering
import com.intsoftdev.nrstations.cache.DBWrapperImpl
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.mock.ClockMock
import com.intsoftdev.nrstations.StationsProxyTest.Companion.MOCK_SERVER_STATIONS_URL
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.assertEquals
import kotlinx.datetime.Clock
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val testModule = module {

    factory(named("HttpTestClientSuccess")) {
        val jsonResponse =
            "[{\"latitude\":51.491060763749196,\"longitude\":0.12139402996250627,\"stationName\":\"Abbey Wood (London)\",\"crsCode\":\"ABW\"}]"

        val engine = MockEngine {
            assertEquals(MOCK_SERVER_STATIONS_URL, it.url.toString())
            respond(
                content = jsonResponse,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        HttpClient(engine = engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }
    }

    factory(named("HttpTestClientError")) {
        val engine = MockEngine {
            assertEquals(MOCK_SERVER_STATIONS_URL, it.url.toString())
            respond(
                content = "",
                status = HttpStatusCode.NotFound
            )
        }

        HttpClient(engine = engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }
    }

    factory<StationsCache> {
        StationsCacheImpl(dbWrapper = get(), settings = get(), clock = get())
    }

    single<Settings> {
        MapSettings()
    }

    single<Clock> {
        ClockMock(Clock.System.now())
    }

    single<DBWrapper> { DBWrapperImpl(get()) }

    single { NRStationsDb(testDbConnection()) }
}
