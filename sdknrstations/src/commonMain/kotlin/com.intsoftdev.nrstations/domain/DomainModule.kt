import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.dsl.module

internal val stationsDomainModule = module(override=true) {
    factory { GetStationsUseCase(get()) }
}