import com.intsoftdev.nrstations.common.APIConfig.Companion.LICENSE_PROP_KEY
import com.intsoftdev.nrstations.common.APIConfig.Companion.SERVER_PROP_KEY
import com.intsoftdev.nrstations.common.DefaultRetryPolicy
import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.StationsProxy
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.domain.StationsRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val stationsDataModule = module(createdAtStart = true) {
    factory(named("NRStationsHttpClient")) {
        HttpClient(engine = get(named("StationsHttpEngine"))) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message)
                    }
                }
                level = LogLevel.HEADERS
            }
        }
    }

    factory<StationsAPI> {
        StationsProxy(
            httpClient = get(named("NRStationsHttpClient")),
            baseUrl = getProperty(SERVER_PROP_KEY),
            azureSubscriptionKey = getProperty(LICENSE_PROP_KEY)
        )
    }

    factory<StationsRepository> {
        StationsRepositoryImpl(
            stationsProxyService = get(),
            stationsCache = get(),
            requestDispatcher = get(named("NRStationsCoroutineDispatcher")),
            requestRetryPolicy = DefaultRetryPolicy()
        )
    }
}
