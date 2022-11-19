package com.intsoftdev.nrstations.viewmodels

import androidx.lifecycle.ViewModel // ktlint-disable import-ordering
import androidx.lifecycle.viewModelScope as androidXViewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope

actual abstract class NreViewModel actual constructor() : ViewModel() {
    actual val viewModelScope: CoroutineScope = androidXViewModelScope

    actual override fun onCleared() {
        Napier.d("onCleared")
        super.onCleared()
    }
}
