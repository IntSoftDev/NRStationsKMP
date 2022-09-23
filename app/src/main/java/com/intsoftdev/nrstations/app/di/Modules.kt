package com.intsoftdev.nrstations.app.di

import com.intsoftdev.nrstations.shared.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        StationsViewModel(get())
    }
}
