package com.intsoftdev.nrstations.model

import kotlinx.serialization.Serializable

@Serializable
data class DataVersion(val version: Double, val lastUpdated: Long)