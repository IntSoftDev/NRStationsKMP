package com.intsoftdev.nrstations.common

data class ClientInfo(
    val platform: String,
    val version: String,
    val build: String
)

interface ClientInfoProvider {
    fun get(): ClientInfo
}

class StaticClientInfoProvider(
    private val info: ClientInfo
) : ClientInfoProvider {
    override fun get(): ClientInfo = info
}
