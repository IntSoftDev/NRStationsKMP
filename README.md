![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/nrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/nrstations)

# NRStations KMP

## About

NRStationsKMP is a library to access UK train stations locations.

## Get Started

## Download
```
repositories {
  mavenCentral()
}
dependencies {
    implementation("com.intsoftdev:nrstations:1.0.0-ALPHA-1")
}
```



### Android

#### 1) Initialise

The library is compiled with JVM 17 bytecode.

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

iOS currently uses local Cocoapods gradle integration.

In the podfile, add the following under the `target` block

```
pod 'nrstations', :path => '~/[PATH_TO_NRStationsKMP/nrstations/]'
```

#### 1) Initialise

In `AppDelegate`, initialise the SDK

```
let stationDefaults = UserDefaults(suiteName: "NRSTATIONS_SETTINGS")!
StationsSDKInitializerKt.doInitStationsSDK(
    apiConfig: DefaultAPIConfig.shared.apiConfig,
    userDefaults: stationDefaults,
    enableLogging: false
)
```
#### 2) Use the ViewModels

```
import nrstations
import KMMViewModelSwiftUI
```

In the SwiftUI View

```
@StateViewModel var stationsViewModel = NrStationsViewModel()
```
