package com.intsoftdev.nrstations.common

/**
 * Representation of a list of stations distances relative to a geo-location point
 *
 * @property geolocation the coordinate relative to which the distance is calculated
 * @property stationDistances the list of stations and their respective distance in miles
 */
data class StationDistances(
    val geolocation: Geolocation,
    val stationDistances: List<StationDistance>
)

/**
 * Representation of a station and it's distance
 *
 * @property station the station whose distance needs to be determined
 * @property distanceInMiles the distance between the station and the reference [geolocation]
 */
data class StationDistance(
    val station: StationLocation,
    val distanceInMiles: Double
)

/**
 * Platform agnostic representation of a Geo-coordinate
 */
data class Geolocation(
    val latitude: Double,
    val longitude: Double
)
