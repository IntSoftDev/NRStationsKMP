package com.intsoftdev.nrstations.cache.entities

import com.intsoftdev.nrstations.common.StationLocation
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.util.UUID

@Serializable
data class StationEntity(
    override val id: String = UUID.randomUUID().toString(),
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
) : Metadata

fun StationEntity.toStationLocation() = StationLocation(
    stationName = this.stationName,
    crsCode = this.crsCode,
    latitude = this.latitude,
    longitude = this.longitude
)

fun StationLocation.toStationEntity() = StationEntity(
    stationName = this.stationName,
    crsCode = this.crsCode,
    latitude = this.latitude,
    longitude = this.longitude
)

@Serializable
data class StationsEntity(override val id: String, val stations: List<StationEntity>) : Metadata

fun StationsEntity.toStationLocations() = this.stations.map {
    it.toStationLocation()
}

fun List<StationLocation>.toStationsEntity() = StationsEntity(
    id = "stationsList",
    stations = this.map {
        it.toStationEntity()
    }
)