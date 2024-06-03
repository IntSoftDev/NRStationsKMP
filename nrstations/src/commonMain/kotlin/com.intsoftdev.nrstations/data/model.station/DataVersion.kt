package com.intsoftdev.nrstations.data.model.station

import com.intsoftdev.nrstations.common.UpdateVersion
import com.intsoftdev.nrstations.database.Version
import kotlinx.serialization.Serializable

@Serializable
internal data class DataVersion(val version: Double, val lastUpdated: Long)

internal fun DataVersion.toVersion() = Version(id = -1, version = this.version, lastUpdate = this.lastUpdated)

internal fun Version.toDataVersion() = DataVersion(version = this.version, lastUpdated = this.lastUpdate)

internal fun DataVersion.toUpdateVersion() = UpdateVersion(version = this.version, lastUpdated = this.lastUpdated)
