package com.example.meteochallenge.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteochallenge.viewmodels.WeatherDataViewModel
import kotlinx.android.synthetic.main.weatherdetail_list.view.*

class WeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
	private val pressure: TextView = view.pressure_value
	private val temperature: TextView = view.temperature_value
	private val rain: TextView = view.humidity_value
	private val wind: TextView = view.wind_value
	private val weatherPic: ImageView = view.weather_pic
	private val weatherState: TextView = view.weather_state

	fun configWeatherDetails(weatherViewModel: WeatherDataViewModel) {
			wind.text = weatherViewModel.getWeatherWindSpeed()
			temperature.text = weatherViewModel.getWeatherMaxMin()
			rain.text = weatherViewModel.getWeatherHumidity()
			pressure.text = weatherViewModel.getWeatherPressure()
			weatherPic.setImageResource(weatherViewModel.getWeatherPic())
			weatherState.text = weatherViewModel.getWeatherState()
	}
}
