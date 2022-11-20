import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.dsl.module

internal val stationsDomainModule = module {
    factory { GetStationsUseCase(get()) }
}
