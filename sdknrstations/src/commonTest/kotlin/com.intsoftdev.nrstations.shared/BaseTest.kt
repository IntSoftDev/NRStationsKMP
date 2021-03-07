package com.intsoftdev.nrstations.shared

import kotlinx.coroutines.CoroutineScope

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect abstract class BaseTest() {
    fun <T> runTest(block: suspend CoroutineScope.() -> T)
}