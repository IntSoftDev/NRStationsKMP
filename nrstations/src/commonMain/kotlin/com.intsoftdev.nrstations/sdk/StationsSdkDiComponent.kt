import com.intsoftdev.nrstations.di.SDKKoinHolder
import com.intsoftdev.nrstations.sdk.NrStationsSDK
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Use this interface to get access to [NrStationsSDK]
 * Call [com.intsoftdev.nrstations.shared.initStationsSDK] to setup the SDK DI
 * then [org.koin.core.component.inject] & [org.koin.core.component.get] will target [SDKKoinHolder]
 */
interface StationsSdkDiComponent : KoinComponent {
    override fun getKoin() = SDKKoinHolder.koinApplication.koin
}

/**
 * Koin API wrapper to get the required component
 * Call [provide] to get the injected object from Koin
 */
inline fun <reified T> StationsSdkDiComponent.provide(): T = get()
