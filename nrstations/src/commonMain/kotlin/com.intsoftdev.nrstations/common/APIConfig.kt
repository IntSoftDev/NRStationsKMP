package com.intsoftdev.nrstations.common

import com.intsoftdev.nrstations.common.APIConfig.Companion.LICENSE_PROP_KEY
import com.intsoftdev.nrstations.common.APIConfig.Companion.SERVER_PROP_KEY

@Suppress("unused")
/**
 * Default Configuration used by iOS
 */
object DefaultAPIConfig {
    val apiConfig = APIConfig()
}

data class APIConfig(
    val serverUrl: String = DEFAULT_SERVER_URL,
    val licenseKey: String? = null,
    val serverToken: String? = null
) {
    companion object {
        const val DEFAULT_SERVER_URL = "https://onrails-test.azurewebsites.net"
        const val SERVER_PROP_KEY = "SERVER_URL"
        const val LICENSE_PROP_KEY = "LICENSE_URL"
        const val SUBSCRIPTION_PROP_KEY = "subscription-key"
    }
}

internal fun APIConfig.toKoinProperties(): Map<String, Any> =
    listOfNotNull(
        SERVER_PROP_KEY to this.serverUrl,
        licenseKey?.let { LICENSE_PROP_KEY to it }
    ).toMap()
