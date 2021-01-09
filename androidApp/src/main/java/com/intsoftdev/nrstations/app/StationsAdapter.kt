package com.intsoftdev.nrstations.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intsoftdev.nrstations.model.StationModel

class StationsAdapter : RecyclerView.Adapter<StationsAdapter.StationViewHolder>() {

    private val stations = mutableListOf<StationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stations_row_layout, parent, false)
        return StationViewHolder(view)
    }

    override fun getItemCount() = stations.size

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position])
    }

    fun updateData(items: List<StationModel>) {
        stations.clear()
        stations.addAll(items)
        notifyDataSetChanged()
    }

    class StationViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(stationModel: StationModel) {
            val stationName = view.findViewById<TextView>(R.id.station_name)
            stationName.text = stationModel.stationName
        }
    }
}