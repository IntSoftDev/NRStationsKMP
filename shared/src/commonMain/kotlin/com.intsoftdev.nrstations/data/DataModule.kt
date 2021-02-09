import com.intsoftdev.nrstations.data.StationsProxyService
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.domain.StationsRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

internal val dataModule = module {
    factory {
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

    factory { StationsProxyService(httpClient = get()) }

    factory<StationsRepository> {
        StationsRepositoryImpl(
            stationsProxyService = get(),
            stationsCache = get(),
            requestDispatcher = get()
        )
    }
}