/*
 * Copyright (C) Integrated Software Development Ltd.
 *
 * Licensed under EUPL-1.2 (see the LICENSE file for the full license governing this code).
 */
package com.intsoftdev.nrstations.sdk

import com.intsoftdev.nrstations.cache.CachePolicy
import com.intsoftdev.nrstations.common.NearestStations
import com.intsoftdev.nrstations.common.StationLocation
import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.shared.CFlow

interface StationsAPI {
    /**
     * Retrieves all the NRE stations from a compatible server
     * The server is typically an instance of [Huxley2](https://github.com/azaka01/Huxley2)
     * This method is an asynchronous [kotlinx.coroutines.flow.Flow]
     *
     * @param cachePolicy specify the [CachePolicy]. Default value is [CachePolicy.USE_CACHE_WITH_EXPIRY]
     * @return [StationsResult] encapsulated in [StationsResultState]
     */
    fun getAllStations(cachePolicy: CachePolicy = CachePolicy.USE_CACHE_WITH_EXPIRY): CFlow<StationsResultState<StationsResult>>

    /**
     * Retrieves a list of [StationLocation] for each of the NRE CRS station alpha codes passed in
     *
     * @param crsCodes vararg of nullable CRS codes
     * the API will only operate on non-null CRS codes
     *
     * @return list of [StationLocation] encapsulated in [StationsResultState]
     */
    suspend fun getStationLocation(vararg crsCodes: String?): CFlow<StationsResultState<List<StationLocation>>>

    /**
     * Returns a list of stations near the geo-location passed in
     * @param [latitude] latitude of geo-location for which nearby stations are required
     * @param [longitude] longitude of geo-location for which nearby stations are required
     *
     * @return an instance of [NearestStations] encapsulated in [StationsResultState]
     * @see [com.intsoftdev.nrstations.data.StationsRepositoryImpl.MAX_NEARBY_STATIONS]
     */
    fun getNearbyStations(
        latitude: Double,
        longitude: Double
    ): CFlow<StationsResultState<NearestStations>>
}
