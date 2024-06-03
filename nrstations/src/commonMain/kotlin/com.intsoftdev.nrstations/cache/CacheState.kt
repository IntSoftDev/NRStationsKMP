package com.intsoftdev.nrstations.cache

internal sealed class CacheState {
    data object Empty : CacheState() // nothing in the cache

    data object Stale : CacheState() // the cache time expiry period has ended or a newer data version is available

    data object Usable : CacheState() // neither of the above states
}
