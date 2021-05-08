package com.intsoftdev.nrstations.app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import co.touchlab.kermit.Kermit
import com.intsoftdev.nrstations.app.ui.StationsViewModel
import com.intsoftdev.nrstations.shared.Greeting
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    protected lateinit var stationsViewModel: StationsViewModel

    private val logger: Kermit by inject()

    private var stationAdapter = StationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stationsViewModel = getViewModel()
        val stationsRecyclerView = findViewById<RecyclerView>(R.id.stationsRecyclerView)

        stationsRecyclerView.adapter = stationAdapter

        val stationsLoading = findViewById<ProgressBar>(R.id.stationsLoading)
        stationsLoading.visibility = View.VISIBLE

        stationsViewModel.stationsLiveData.observe(
            this,
            Observer { stations ->
                stationAdapter.updateData(stations)
                stationsLoading.visibility = View.GONE
            }
        )
        stationsViewModel.errorLiveData.observe(
            this,
            Observer { errorMessage ->
                logger.e { "error $errorMessage" }
                stationsLoading.visibility = View.GONE
                Toast.makeText(this, "Error $errorMessage", LENGTH_SHORT).show()
            }
        )
        logger.d { "getAllStations" }
        stationsViewModel.getAllStations()
    }
}
