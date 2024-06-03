import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val stationsDomainModule =
    module {
        factory {
            GetStationsUseCase(
                stationsRepository = get(),
                coroutineDispatcher = get(named("NRStationsCoroutineDispatcher"))
            )
        }
    }
