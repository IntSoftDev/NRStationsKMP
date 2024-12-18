package com.intsoftdev.nrstations.shared

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.di.KOIN_HTTP_CLIENT
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val stationsPlatformModule =
    module {
        single<SqlDriver>(named("StationsSqlDriver")) {
            NativeSqliteDriver(
                NRStationsDb.Schema,
                "NRStationsDb"
            )
        }

        factory(named(KOIN_HTTP_CLIENT)) {
            HttpClient(Darwin) {

                engine {
                    configureRequest {
                        this.setTimeoutInterval(HTTP_TIMEOUT_SECONDS)
                    }
                    configureSession {
                        this.timeoutIntervalForRequest = HTTP_TIMEOUT_SECONDS
                        this.timeoutIntervalForResource = HTTP_TIMEOUT_SECONDS
                    }
                }

                install(ContentNegotiation) {
                    json(nonStrictJson)
                }
                install(Logging) {
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                Napier.d(message)
                            }
                        }
                    level = LogLevel.INFO
                }
            }
        }
    }
