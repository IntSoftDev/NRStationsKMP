package com.intsoftdev.nrstations.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.intsoftdev.nrstations.shared.Greeting
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.google.android.material.snackbar.Snackbar
import com.intsoftdev.nrstations.android.ui.StationsViewModel
import kotlinx.android.synthetic.main.activity_main.*
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

    private var stationAdapter = StationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.d("NRStations enter")
        viewModel = getViewModel()
        setContentView(R.layout.activity_main)

        stationsRecyclerView.adapter = stationAdapter

        stationsLoading.visibility = View.VISIBLE

        viewModel.stationsLiveData.observe(
            this,
            Observer { stations ->
                stationAdapter.updateData(stations)
                stationsLoading.visibility = View.GONE
            }
        )
        viewModel.errorLiveData.observe(
            this,
            Observer { errorMessage ->
                Napier.e("error $errorMessage")
                stationsLoading.visibility = View.GONE
                Toast.makeText(this, "Error $errorMessage", LENGTH_SHORT).show()
            }
        )
        viewModel.getStationsFromNetwork()
    }
}
