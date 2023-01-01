package com.intsoftdev.nrstations.common

/**
 * Encapsulation of any SDK method responses
 * The caller should query this before proceeding
 */
sealed class StationsResultState<out T : Any> {
    /**
     * The method response succeeded and the required data is available
     * @param data reified class that stores the result data
     */
    data class Success<out T : Any>(val data: T) : StationsResultState<T>()

    /**
     * The method response failed and an exception was thrown
     * @param error an optional instance of [Throwable] the client can use to determine the cause of failure
     */
    data class Failure(val error: Throwable?) : StationsResultState<Nothing>()
}
