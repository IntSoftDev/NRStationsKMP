![GitHub Workflow Status](https://img.shields.io/github/workflow/status/IntSoftDev/NRStationsKMP/NRStations-Android)
[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/nrstations?label=Maven%20Central)](https://search.maven.org/artifact/com.intsoftdev/nrstations)

# NRStations SDK

## About

NRStations is a Kotlin Multiplatform library and SDK.

## Get Started

Add this in your app's build.gradle file:

```
dependencies {
    implementation 'com.intsoftdev:nrstations:0.0.1'
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
