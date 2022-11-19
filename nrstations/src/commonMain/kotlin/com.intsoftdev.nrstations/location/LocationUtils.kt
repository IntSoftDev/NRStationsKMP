package com.intsoftdev.nrstations.location

import com.intsoftdev.nrstations.common.Geolocation
import com.intsoftdev.nrstations.common.StationDistances
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsDistance
import com.intsoftdev.nrstations.common.StationsList
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

internal fun getSortedStations(
    latitude: Double,
    longitude: Double,
    allStations: List<StationLocation>
): List<StationLocation> {
    val stationComparator = Comparator<StationLocation> { station1, station2 ->
        val distanceFromStation1 =
            distanceInMetres(station1.latitude, station1.longitude, latitude, longitude)
        val distanceFromStation2 =
            distanceInMetres(station2.latitude, station2.longitude, latitude, longitude)

        val diff = distanceFromStation1 - distanceFromStation2
        diff.toInt()
    }
    return allStations.sortedWith(stationComparator)
}

internal fun getStationDistancesfromRefPoint(
    geolocation: Geolocation,
    stationsList: StationsList
): StationDistances =
    mapOf(
        geolocation to stationsList.map {
            it.toStationDistance(geolocation)
        }
    )

private fun StationLocation.toStationDistance(geolocation: Geolocation): StationsDistance {
    val distance =
        distanceInMiles(geolocation.latitude, geolocation.longitude, latitude, longitude)
    return StationsDistance(this, distance)
}

private fun distanceInMiles(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val theta = lon1 - lon2
    var dist =
        sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
            deg2rad(theta)
        )
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    return dist
}

private fun distanceInMetres(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    return distanceInMiles(lat1, lon1, lat2, lon2) * 1609.344
}

private fun deg2rad(deg: Double): Double {
    return deg * PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / PI
}
