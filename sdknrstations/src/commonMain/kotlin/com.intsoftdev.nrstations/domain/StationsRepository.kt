package com.intsoftdev.nrstations.domain

import com.intsoftdev.nrstations.common.StationsResult
import com.intsoftdev.nrstations.common.StationsResultState
import com.intsoftdev.nrstations.shared.CFlow
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This specifies the operations that need to be implemented in the data layer
 */
internal interface StationsRepository {
    fun getAllStations(): Flow<StationsResultState<StationsResult>>
}