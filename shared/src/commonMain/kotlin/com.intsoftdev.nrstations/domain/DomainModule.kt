import com.intsoftdev.nrstations.domain.GetStationsUseCase
import org.koin.dsl.module

internal val domainModule = module {
    factory { GetStationsUseCase(get()) }
}