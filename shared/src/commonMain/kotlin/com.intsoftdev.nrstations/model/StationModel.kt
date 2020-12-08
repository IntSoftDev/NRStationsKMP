package com.intsoftdev.nrstations.model

import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.util.UUID

@Serializable
data class StationModel(
    override val id: String = UUID.randomUUID().toString(),
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
) : Metadata

data class StationsResult(
    val version: DataVersion,
    val stations : List<StationModel>
)

@Serializable
data class StationsList(override val id: String, val stations: List<StationModel>) : Metadata