import com.intsoftdev.nrstations.common.APIConfig.Companion.LICENSE_PROP_KEY
import com.intsoftdev.nrstations.common.APIConfig.Companion.SERVER_PROP_KEY
import com.intsoftdev.nrstations.common.DefaultRetryPolicy
import com.intsoftdev.nrstations.data.StationsAPI
import com.intsoftdev.nrstations.data.StationsProxy
import com.intsoftdev.nrstations.data.StationsRepositoryImpl
import com.intsoftdev.nrstations.di.KOIN_HTTP_CLIENT
import com.intsoftdev.nrstations.domain.StationsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val stationsDataModule =
    module {

        factory<StationsAPI> {
            StationsProxy(
                httpClient = get(named(KOIN_HTTP_CLIENT)),
                baseUrl = getProperty(SERVER_PROP_KEY),
                azureSubscriptionKey = getPropertyOrNull(LICENSE_PROP_KEY)
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
