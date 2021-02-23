package com.intsoftdev.nrstations.data.model.station

import com.intsoftdev.nrstations.common.UpdateVersion
import kotlinx.serialization.Serializable

@Serializable
internal data class DataVersion(val version: Double, val lastUpdated: Long)

internal fun DataVersion.toUpdateVersion() =
    UpdateVersion(version = this.version, lastUpdated = this.lastUpdated)