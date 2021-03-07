import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.StationsProxy
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.domain.StationsRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

internal val stationsDataModule = module {
    factory(named("NRStationsHttpClient")) {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(nonStrictJson)
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
            requestDispatcher = get(named("NRStationsCoroutineDispatcher"))
        )
    }
}