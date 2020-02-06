package com.example.meteochallenge.views.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meteochallenge.models.Weather

class WeatherAdapter(private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private var data: Weather? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemCount(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	fun setData(weather: Weather) {
		data = weather
	}
}