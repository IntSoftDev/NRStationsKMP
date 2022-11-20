package com.intsoftdev.nrstations.common

/**
 * Representation of the Stations API version used and time it was last updated
 * @property version server-side version of the stations API used. This is incremented each time
 * a change is made for instance when a new station(s) is added
 * @property lastUpdated Epoch time when the last update was done
 */
data class UpdateVersion(
    val version: Double,
    val lastUpdated: Long
)
