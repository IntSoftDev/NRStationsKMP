package com.intsoftdev.nrstations.common

typealias StationsList = List<StationLocation>

data class StationsResult(
    val version: UpdateVersion,
    val stations: StationsList
)
