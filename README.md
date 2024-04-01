![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/nrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/nrstations)

# NRStations KMP

A Kotlin library to access UK train stations locations from Android and iOS devices.

## Adding to your project
```
repositories {
  mavenCentral()
}
dependencies {
    implementation("com.intsoftdev:nrstations:1.0.0-ALPHA-2")
}
```

The demo project also uses this structure.

## Android

#### 1) Setup and initialise

The library is compiled with JVM 17.

Add this to app level build gradle

```
kotlin {
    jvmToolchain(17)
}
```

In the `manifest.xml`, ensure the following permission is available:

`android.permission.INTERNET`

Initialise the SDK in the `Application` class

```
 initStationsSDK(
      context = this
  )
```

#### 2) Inject the SDK

Create your own ViewModel or use one of the existing KMP ViewModels.

If using your own VM, derive it from `StationsSdkDiComponent`.

Then inject the SDK.

```
private val stationsSDK = this.injectStations<NrStationsSDK>()
```

#### 3) Use the API

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

Add [KMMViewModel](https://github.com/rickclephas/KMM-ViewModel) `1.0.0-ALPHA-19` to the iOS package dependencies.

Add the following KMM ViewModel extension in the project - see the sample app for more details:

```
extension Kmm_viewmodel_coreKMMViewModel: KMMViewModel { }
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
import KMMViewModelSwiftUI
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

## Useful Links

https://github.com/touchlab/KaMPKit

https://github.com/joreilly

https://github.com/rickclephas/KMM-ViewModel

## License

Licensed under the [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence).

The EUPL covers distribution through a network or SaaS (like a compatible and interoperable AGPL).
