import com.intsoftdev.nrstations.common.DefaultRetryPolicy
import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.StationsProxy
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.domain.StationsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

internal val stationsDataModule = module {
    factory(named("NRStationsHttpClient")) {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }

    factory<StationsAPI> { StationsProxy(httpClient = get(named("NRStationsHttpClient"))) }

    factory<StationsRepository> {
        StationsRepositoryImpl(
            stationsProxyService = get(),
            stationsCache = get(),
            requestDispatcher = get(named("NRStationsCoroutineDispatcher")),
            requestRetryPolicy = DefaultRetryPolicy()
        )
    }
}
