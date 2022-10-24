package com.intsoftdev.nrstations.shared

import kotlinx.coroutines.CoroutineScope

expect abstract class SdkViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}
