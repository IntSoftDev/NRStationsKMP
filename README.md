[![NRStations SDK](https://img.shields.io/github/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android/main?logo=Android&style=plastic)](https://github.com/IntSoftDev/NRStationsKMP/actions/workflows/NRStations-Android.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/sdknrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/sdknrstations)

# NRStations SDK

## About

NRStations is a Kotlin Multiplatform library and SDK.

## Get Started

Add this in your app's build.gradle file:

```
dependencies {
    implementation 'com.intsoftdev:sdknrstations:3.0.2'
}
```
The SDK uses `Koin` for dependency injection. 

The Koin application can be passed as an optional parameter if available.

In the `Application::OnCreate` class add these lines
```
initStationsSDK(context = this, koinApp = koinApp, enableLogging = true)
```

Or let the SDK create its own DI graph internally

```
initStationsSDK(context = this)
```
