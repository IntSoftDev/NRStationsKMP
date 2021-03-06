package com.intsoftdev.nrstations.common

data class UpdateVersion(val version: Double, val lastUpdated: Long)

data class StationLocation( val stationName: String,
                            val crsCode: String,
                            val latitude: Double,
                            val longitude: Double)

data class StationsResult(
    val version: UpdateVersion,
    val stations : List<StationLocation>
)
