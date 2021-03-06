package com.intsoftdev.nrstations.shared

import kotlinx.coroutines.CoroutineScope

expect abstract class BaseTest() {
    fun <T> runTest(block: suspend CoroutineScope.() -> T)
}