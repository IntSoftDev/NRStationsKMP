package com.intsoftdev.nrstations.location

import com.intsoftdev.nrstations.common.Geolocation
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationDistance
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsList
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Utility method for calculating the distance between [StationLocation] and a triggering [Geolocation]
 */
@Suppress("unused")
fun StationLocation.distanceInMetres(geolocation: Geolocation): Double {
    return distanceInMetresHaversine(
        geolocation.latitude,
        geolocation.longitude,
        this.latitude,
        this.longitude
    )
}

internal fun getSortedStations(
    latitude: Double,
    longitude: Double,
    allStations: List<StationLocation>
): List<StationLocation> {
    val stationComparator =
        Comparator<StationLocation> { station1, station2 ->
            val distanceFromStation1 =
                distanceInMetresHaversine(
                    station1.latitude,
                    station1.longitude,
                    latitude,
                    longitude
                )
            val distanceFromStation2 =
                distanceInMetresHaversine(
                    station2.latitude,
                    station2.longitude,
                    latitude,
                    longitude
                )

            val diff = distanceFromStation1 - distanceFromStation2
            diff.toInt()
        }
    return allStations.sortedWith(stationComparator)
}

internal fun getStationDistancesfromRefPoint(
    geolocation: Geolocation,
    stationsList: StationsList
) = NearestStations(
    geolocation = geolocation,
    stationDistances = stationsList.map { it.toStationDistance(geolocation) }
)

private fun StationLocation.toStationDistance(geolocation: Geolocation): StationDistance {
    val distanceInMiles =
        distanceInMetresHaversine(
            geolocation.latitude,
            geolocation.longitude,
            latitude,
            longitude
        ) / 1609.344F
    return StationDistance(this, distanceInMiles)
}

private fun distanceInMetresHaversine(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val earthRadius = 6371000.0 // in metres

    val dLat = deg2rad(lat2 - lat1)
    val dLon = deg2rad(lon2 - lon1)

    val a = sin(dLat / 2).pow(2) + (cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * sin(dLon / 2).pow(2))

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

private fun deg2rad(deg: Double): Double {
    return deg * PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / PI
}
