package com.intsoftdev.nrstations.shared

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Use this rule to update the Main dispatcher ahead of tests. By delegating the main dispatcher to a new thread.
 * we can block the current thread and still dispatch main coroutines
 */
class CoroutineTestRule @ExperimentalCoroutinesApi constructor(
    private val testDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext("UI thread")
) : TestWatcher() {

    @ExperimentalCoroutinesApi
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
