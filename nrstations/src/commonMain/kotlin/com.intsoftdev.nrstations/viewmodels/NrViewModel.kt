package com.intsoftdev.nrstations.viewmodels

import kotlinx.coroutines.CoroutineScope

expect abstract class NrViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}
