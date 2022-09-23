package com.intsoftdev.nrstations.cache

internal sealed class CacheState {
    object Empty : CacheState() // nothing in the cache
    object Stale : CacheState() // the cache time expiry period has ended or a newer data version is available
    object Usable : CacheState() // neither of the above states
}
