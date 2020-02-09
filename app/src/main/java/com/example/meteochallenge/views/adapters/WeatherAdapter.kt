package com.example.meteochallenge.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meteochallenge.R
import com.example.meteochallenge.viewholders.WeatherViewHolder
import com.example.meteochallenge.viewmodels.WeatherDataViewModel

class WeatherAdapter(private val context: Context
) : RecyclerView.Adapter<WeatherViewHolder>() {
	var data: WeatherDataViewModel? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val view = inflater.inflate(R.layout.weatherdetail_list, parent, false)

		return WeatherViewHolder(view)
	}

	override fun getItemCount(): Int {
		return 1
	}

	override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
		data?.let {
			val obj = it
			holder.configWeatherDetails(obj)
		}
	}
}