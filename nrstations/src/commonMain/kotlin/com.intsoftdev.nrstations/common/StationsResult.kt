package com.intsoftdev.nrstations.common

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

data class UpdateVersion(val version: Double, val lastUpdated: Long)

@Parcelize
data class StationLocation(
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable

data class StationsResult(
    val version: UpdateVersion,
    val stations: List<StationLocation>
)
