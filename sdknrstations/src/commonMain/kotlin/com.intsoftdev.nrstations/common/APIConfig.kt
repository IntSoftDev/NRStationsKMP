package com.intsoftdev.nrstations.common

import com.intsoftdev.nrstations.common.APIConfig.Companion.SERVER_PROP_KEY
import com.intsoftdev.nrstations.common.APIConfig.Companion.lICENSE_PROP_KEY

data class APIConfig(
    val serverUrl: String = DEFAULT_SERVER_URL,
    val licenseKey: String? = null
) {
    companion object {
        const val DEFAULT_SERVER_URL = "https://onrails-test.azurewebsites.net"
        const val SERVER_PROP_KEY = "SERVER_URL"
        const val lICENSE_PROP_KEY = "LICENSE_URL"
    }
}

internal fun APIConfig.toKoinProperties(): Map<String, Any> =
    listOfNotNull(
        SERVER_PROP_KEY to this.serverUrl,
        licenseKey?.let { lICENSE_PROP_KEY to it }
    ).toMap()
