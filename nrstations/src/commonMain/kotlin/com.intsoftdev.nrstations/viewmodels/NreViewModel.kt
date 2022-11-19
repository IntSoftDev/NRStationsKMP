package com.intsoftdev.nrstations.viewmodels

import kotlinx.coroutines.CoroutineScope

expect abstract class NreViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}
