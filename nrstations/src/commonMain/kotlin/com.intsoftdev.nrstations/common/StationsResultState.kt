package com.intsoftdev.nrstations.common

sealed class StationsResultState<out T : Any> {
    data class Success<out T : Any>(val data: T) : StationsResultState<T>()
    data class Failure(val error: Throwable?) : StationsResultState<Nothing>()
}
