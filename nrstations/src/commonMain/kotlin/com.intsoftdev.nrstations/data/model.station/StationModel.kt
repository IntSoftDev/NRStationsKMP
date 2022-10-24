package com.intsoftdev.nrstations.data.model.station

import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.database.StationDb
import kotlinx.serialization.Serializable

/*
 representations of data returned by the server API
*/

@Serializable
internal data class StationModel(
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
)

internal fun StationModel.toStationLocation() = StationLocation(
    crsCode = this.crsCode,
    stationName = this.stationName,
    latitude = this.latitude,
    longitude = this.longitude
)

internal fun StationLocation.toNRStation() = StationDb(
    id = getHashFromStation(this.crsCode, this.stationName),
    crs = this.crsCode,
    name = this.stationName,
    latitude = this.latitude,
    longitude = this.longitude
)

internal fun getHashFromStation(crs: String, name: String): Long {
    return (crs + name).hashCode().toLong()
}

internal fun StationDb.toStationLocation() = StationLocation(
    crsCode = this.crs,
    stationName = this.name,
    latitude = this.latitude,
    longitude = this.longitude
)
