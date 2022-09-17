package com.intsoftdev.nrstations.shared

import org.koin.core.module.Module

internal expect fun getApplicationFilesDirectoryPath(): String

internal expect val nrStationsPlatformModule: Module
