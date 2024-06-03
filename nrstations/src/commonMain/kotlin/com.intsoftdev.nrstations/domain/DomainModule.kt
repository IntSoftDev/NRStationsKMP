import com.intsoftdev.nrstations.domain.GetStationsUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

internal val stationsDomainModule =
    module {
        factory {
            GetStationsUseCase(
                stationsRepository = get(),
                coroutineDispatcher = Dispatchers.Default
            )
        }
    }
