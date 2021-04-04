package com.intsoftdev.nrstations.shared

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@RunWith(AndroidJUnit4::class)
actual abstract class BaseTest {
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    actual fun <T> runTest(block: suspend CoroutineScope.() -> T) {
        runBlocking { block() }
    }
}
