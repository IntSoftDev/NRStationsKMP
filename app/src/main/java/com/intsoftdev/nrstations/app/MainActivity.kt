package com.intsoftdev.nrstations.app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.intsoftdev.nrstations.shared.Greeting
import com.intsoftdev.nrstations.shared.StationsViewModel
import com.intsoftdev.nrstations.shared.StationsViewState
import io.github.aakira.napier.Napier
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
        stationsLoading.visibility = View.GONE

        collectUIData()

        stationsViewModel.getAllStations()
    }

    private fun collectUIData() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                stationsViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        StationsViewState.Loading -> {
                        }

                        is StationsViewState.StationsLoaded -> {
                            Napier.d("StationsLoaded ${uiState.stationsData.count()}")
                            stationAdapter.updateData(uiState.stationsData)
                        }

                        is StationsViewState.Error -> {
                            Napier.d("Error ${uiState.throwable}")
                        }
                    }
                }
            }
        }
    }
}
