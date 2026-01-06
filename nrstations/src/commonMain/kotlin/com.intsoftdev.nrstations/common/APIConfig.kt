/*
 * Copyright (C) Integrated Software Development Ltd.
 *
 * Licensed under EUPL-1.2 (see the LICENSE file for the full license governing this code).
 */

package com.intsoftdev.nrstations.common

import com.intsoftdev.nrstations.common.APIConfig.Companion.SERVER_PROP_KEY

/**
 * Default Configuration used by iOS
 */
@Suppress("unused")
object DefaultAPIConfig {
    val apiConfig = APIConfig()
}

/**
 * Server side configuration
 *
 * @param serverUrl Url hosting the stations service.
 * Default Url [APIConfig.DEFAULT_SERVER_URL] should never be used in production
 * Apps can set up their own proxy service from instance of [Huxley2](https://github.com/azaka01/Huxley2)
 *
 * @param Darwin NRE serverToken provided by NRE to access their API. Huxley uses this to send real-time requests.
 * @param apiKey API key for accessing the Journey Planner service
 */
data class APIConfig(
    val serverUrl: String = DEFAULT_SERVER_URL,
    val serverToken: String? = null,
    val apiKey: String? = null,
    val clientInfo: ClientInfo? = null
) {
    companion object {
        const val DEFAULT_SERVER_URL = "https://onrails-test.azurewebsites.net"
        const val SERVER_PROP_KEY = "SERVER_URL"
    }
}

internal fun APIConfig.toKoinProperties(): Map<String, Any> =
    listOfNotNull(
        SERVER_PROP_KEY to this.serverUrl
    ).toMap()
