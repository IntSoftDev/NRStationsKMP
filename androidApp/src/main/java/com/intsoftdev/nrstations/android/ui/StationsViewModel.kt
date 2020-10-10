package com.intsoftdev.nrstations.android.ui

import androidx.lifecycle.ViewModel
import com.github.aakira.napier.Napier

class StationsViewModel : ViewModel() {

    init {
        Napier.d("init")
    }

    fun test() {
        Napier.d("test")
    }
}