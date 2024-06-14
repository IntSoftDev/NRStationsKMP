package com.intsoftdev.nrstations.shared

import android.app.Application
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.intsoftdev.nrstations.database.NRStationsDb
import com.intsoftdev.nrstations.di.KOIN_HTTP_CLIENT
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import java.util.concurrent.TimeUnit
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal lateinit var appContext: Context

internal actual val stationsPlatformModule =
    module {

        factory<Settings> {
            SharedPreferencesSettings(
                appContext.getSharedPreferences(
                    "NRSTATIONS_SETTINGS",
                    Application.MODE_PRIVATE
                )
            )
        }

        single<SqlDriver>(named("StationsSqlDriver")) {
            AndroidSqliteDriver(
                schema = NRStationsDb.Schema,
                context = appContext,
                name = "NRStationsDb"
            )
        }

        factory(named(KOIN_HTTP_CLIENT)) {
            HttpClient(OkHttp) {

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

                this.engine {
                    this.config {
                        callTimeout(HTTP_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                        connectTimeout(HTTP_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                        readTimeout(HTTP_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                        writeTimeout(HTTP_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                    }
                }
            }
        }
    }
