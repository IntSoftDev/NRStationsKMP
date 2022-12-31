package com.intsoftdev.nrstations.common

/**
 * Representation of a train station
 * @property stationName the presentable name of a station
 * @property crsCode 3 letter alpha code to uniquely represent a station.
 * These are standardised by UK National Rail Enquiries
 * @property latitude Latitude of the station geo-location
 * @property longitude Longitude of the station geo-location
 */
data class StationLocation(
    val stationName: String,
    val crsCode: String,
    val latitude: Double,
    val longitude: Double
)
