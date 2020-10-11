package com.intsoftdev.nrstations.model

import kotlinx.serialization.Serializable

@Serializable
data class StationModel(
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
)