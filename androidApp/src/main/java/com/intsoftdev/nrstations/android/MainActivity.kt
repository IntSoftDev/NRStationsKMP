package com.intsoftdev.nrstations.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intsoftdev.nrstations.shared.Greeting
import android.widget.TextView
import androidx.activity.viewModels
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.intsoftdev.nrstations.android.ui.StationsViewModel
import org.koin.androidx.viewmodel.compat.ScopeCompat.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity(), KoinComponent {

    private lateinit var viewModel :StationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.d("NRStations enter")
        viewModel = getViewModel()
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()

        viewModel.test()
    }
}
