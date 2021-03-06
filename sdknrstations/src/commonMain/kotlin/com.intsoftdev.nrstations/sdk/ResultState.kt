package com.intsoftdev.nrstations.sdk

sealed class ResultState<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultState<T>()
    class Loading<out T: Any> : ResultState<T>()
    data class Failure(val error: Throwable?) : ResultState<Nothing>()
}