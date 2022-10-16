package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.cache.DBWrapperImpl
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.mock.ClockMock
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.test.assertEquals

internal val testModule = module {

    factory(named("HttpTestClientSuccess")) {
        val jsonResponse =
            "[{\"latitude\":51.491060763749196,\"longitude\":0.12139402996250627,\"stationName\":\"Abbey Wood (London)\",\"crsCode\":\"ABW\"}]"

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

        HttpClient(engine = engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }
    }

    factory(named("HttpTestClientError")) {
        val engine = MockEngine {
            assertEquals("https://onrails.azurewebsites.net/stations", it.url.toString())
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

    single{NRStationsDb(testDbConnection()) }
}