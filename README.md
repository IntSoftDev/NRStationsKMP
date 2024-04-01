![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/nrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/nrstations)

# NRStations KMP

A library to access UK train stations locations.

## Get Started

## Download
```
repositories {
  mavenCentral()
}
dependencies {
    implementation("com.intsoftdev:nrstations:1.0.0-ALPHA-2")
}
```

The demo project also uses this structure.

### Android

#### 1) Initialise

The library is compiled with JVM 17.

Add this to app level build gradle

```
kotlin {
    jvmToolchain(17)
}
```

Initialise in the `Application` class

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

### iOS

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
```

## License

Licensed under the [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence).

The EUPL covers distribution through a network or SaaS (like a compatible and interoperable AGPL).
