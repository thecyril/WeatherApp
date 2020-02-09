package com.example.meteochallenge.viewmodels

import com.example.meteochallenge.models.WeatherState

class WeatherDataViewModel(
		private val category: WeatherState,
		private val pressure: String,
		private val tempMinMax: String,
		private val windSpeed: String,
		private val humidity: String) {

	fun getWeatherPressure(): String = pressure

	fun getWeatherMaxMin(): String = tempMinMax

	fun getWeatherWindSpeed(): String = windSpeed

	fun getWeatherHumidity(): String = humidity

	fun getWeatherState(): String = category.label

	fun getWeatherPic(): Int = category.picture
}