package com.intsoftdev.nrstations.shared

import org.koin.core.module.Module

internal expect class Platform() {
    val platform: String
}

internal expect fun getApplicationFilesDirectoryPath(): String

internal expect val nrStationsPlatformModule: Module