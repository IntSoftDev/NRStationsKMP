package com.intsoftdev.nrstations.shared

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

internal expect val stationsPlatformModule: Module

internal val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

internal const val HTTP_TIMEOUT_SECONDS = 30.0
