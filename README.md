![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/nrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/nrstations)

# NRStations KMP

A Kotlin library to retrieve all UK train stations and their locations from Android and iOS devices.

## Adding to your project
```
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.intsoftdev:nrstations:1.0.0-ALPHA-x")
}
```

The sample project also uses this structure.

## Android

#### 1) Setup

The library is compiled with JVM 17 so the relevant `-jvm-target` needs to be set.

This can be added to app level `build.gradle.kts`

```
kotlin {
    jvmToolchain(17)
}
```

In the `manifest.xml`, ensure the following permission is available:

`android.permission.INTERNET`

#### 2) Initialise

Initialise the SDK in the `Application` class

```
 initStationsSDK(
      context = this
  )
```

#### 3) Inject the SDK

Create your own ViewModel or use one of the existing KMP ViewModels.

If using your own VM, derive it from `StationsSdkDiComponent`.

Then inject the SDK.

```
private val stationsSDK = injectStations<NrStationsSDK>()
```

#### 4) Use the API

The API uses Flow which encapsulates a `StationsResultState` to indicate success or failure.

#### All Stations

```
stationsSDK.getAllStations().collect { stationsResult ->
    when (stationsResult) {
        is StationsResultState.Success -> {}
        is StationsResultState.Failure -> {}
    }
}
```

#### Nearby Stations

```
stationsSDK.getNearbyStations(latitude: Double, longitude: Double)

```

## iOS

#### 1) Pod setup

iOS currently uses local Cocoapods gradle integration.

In the podfile, add the following under the `target` block

```
pod 'nrstations', :path => '~/[PATH_TO_NRStationsKMP/nrstations/]'
```

If the pod file doesn't exist, then create a [new one](https://github.com/touchlab/KaMPKit/blob/main/docs/IOS_PROJ_INTEGRATION.md#create-podfile)
and run `pod install`.

#### 2) Add package dependency

Add [KMMObservableViewModel](https://github.com/rickclephas/KMP-ObservableViewModel) to the iOS package dependencies.

Add the following KMM ViewModel extension in the project - see the sample app for more details:

```
extension Kmp_observableviewmodel_coreViewModel: ViewModel { }
```

#### 3) Initialise

In `AppDelegate`, initialise the SDK

```
StationsSDKInitializerKt.doInitStationsSDK(
    apiConfig: DefaultAPIConfig.shared.apiConfig,
    enableLogging: false
)
```
#### 4) Use the ViewModels

```
import nrstations
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI
```

In the SwiftUI View

```
@StateViewModel var stationsViewModel = NrStationsViewModel()

stationsViewModel.getAllStations()

switch stationsViewModel.uiState {
    case let uiState as StationsUiStateLoaded:
        // update view with Stations
    case let uiState as StationsUiStateError:
       // handle error
}
```

## Configuration

The library utilises an actual testing service that includes all train stations across the UK mainland, along with a few connected via EuroStar. Updates are made whenever a new station is opened.

Sample applications can utilise this, but for production applications, it is advisable to create a dedicated instance from this [link](https://github.com/azaka01/Huxley2).

An `APIConfig` object can be passed into to `initStationsSDK` to configure the server URL.

## Sample Apps

### Android (Jetpack Compose)

You can choose to add `MAPS_API_KEY=<your key>` in `local.properties` to enable Google Maps.

https://github.com/IntSoftDev/NRStationsKMP/assets/1098487/d91b7182-f8a3-4846-96fd-683a4ae6911c

### iOS (Swift UI)

https://github.com/IntSoftDev/NRStationsKMP/assets/1098487/9d17998b-3d48-4bd5-95e2-4327b2acefa6

## Languages and Tools used

[Kotlin](https://kotlinlang.org/)  
[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)  
[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)  
[Ktor client library](https://github.com/ktorio/ktor)  
[Android Architecture Components](https://developer.android.com/topic/libraries/architecture)  
[Koin](https://github.com/InsertKoinIO/koin)  
[Jetpack Compose](https://developer.android.com/jetpack/compose)  
[SwiftUI](https://developer.apple.com/documentation/swiftui)  
[KMM-ViewModel](https://kotlinlang.org/lp/mobile/static/kmm-viewmodel/)  
[SQLDelight](https://github.com/cashapp/sqldelight)  
[Turbine](https://github.com/cashapp/turbine)  
[Napier Logger](https://github.com/AAkira/Napier)  
[Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)  

## License

Licensed under the [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence).

The EUPL covers distribution through a network or SaaS (like a compatible and interoperable AGPL).
