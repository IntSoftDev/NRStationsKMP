package com.intsoftdev.nrstations

import com.intsoftdev.nrstations.StationsProxyTest.Companion.MOCK_SERVER_STATIONS_URL
import com.intsoftdev.nrstations.cache.DBWrapper
import com.intsoftdev.nrstations.cache.DBWrapperImpl
import com.intsoftdev.nrstations.cache.StationsCache
import com.intsoftdev.nrstations.cache.StationsCacheImpl
import com.intsoftdev.nrstations.common.DefaultRetryPolicy
import com.intsoftdev.nrstations.data.StationsProxy
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.domain.StationsRepository
import com.intsoftdev.nrstations.mock.ClockMock
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
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val testModule =
    module {

        factory(named("HttpTestClientSuccess")) {
            val locationJson = "[{\"latitude\":51.491060763749196,\"longitude\":0.12139402996250627,"
            val stationJson = "\"stationName\":\"Abbey Wood (London)\",\"crsCode\":\"ABW\"}]"
            val jsonResponse = locationJson + stationJson

            val engine =
                MockEngine {
                    assertEquals(MOCK_SERVER_STATIONS_URL, it.url.toString())
                    respond(
                        content = jsonResponse,
                        headers =
                            headersOf(
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
            val engine =
                MockEngine {
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

        factory<StationsRepository> {
            val httpClientSuccess = get<HttpClient>(named("HttpTestClientSuccess"))
            val stationsApi =
                StationsProxy(httpClientSuccess, StationsProxyTest.MOCK_SERVER_BASE_URL)

            StationsRepositoryImpl(
                stationsProxyService = stationsApi,
                stationsCache = get(),
                requestDispatcher = Dispatchers.Default,
                requestRetryPolicy = DefaultRetryPolicy()
            )
        }
    }
