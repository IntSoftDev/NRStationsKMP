package com.intsoftdev.nrstations.common

typealias StationsList = List<StationLocation>

/**
 * Representation of a response to retrieve all stations
 * @property version an instance of [UpdateVersion]
 * @property stations a list of [StationLocation]
 */
data class StationsResult(
    val version: UpdateVersion,
    val stations: StationsList
)
