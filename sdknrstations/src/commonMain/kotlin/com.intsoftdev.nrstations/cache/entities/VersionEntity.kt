package com.intsoftdev.nrstations.cache.entities

import com.intsoftdev.nrstations.common.UpdateVersion
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.util.UUID

@Serializable
internal data class VersionEntity(
    override val id: String = UUID.randomUUID().toString(),
    val version: Double,
    val lastUpdate: Long
) : Metadata

internal fun UpdateVersion.toVersionEntity() =
    VersionEntity(version = this.version, lastUpdate = this.lastUpdated)

internal fun VersionEntity.toUpdateVersion() =
    UpdateVersion(version = this.version, lastUpdated = this.lastUpdate)