package com.intsoftdev.nrstations.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.intsoftdev.nrstations.shared.Greeting
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import io.github.aakira.napier.Napier
import com.intsoftdev.nrstations.app.ui.StationsViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    protected lateinit var stationsViewModel: StationsViewModel

    private var stationAdapter = StationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.d("NRStations enter")
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
                Napier.e("error $errorMessage")
                stationsLoading.visibility = View.GONE
                Toast.makeText(this, "Error $errorMessage", LENGTH_SHORT).show()
            }
        )
        stationsViewModel.getAllStations()
    }
}
