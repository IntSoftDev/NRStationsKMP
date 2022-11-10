package com.intsoftdev.nrstations.app.di

import com.intsoftdev.nrstations.app.ui.StationsViewModel
import com.intsoftdev.nrstations.shared.StationsSDKViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        StationsSDKViewModel(get())
    }
}
