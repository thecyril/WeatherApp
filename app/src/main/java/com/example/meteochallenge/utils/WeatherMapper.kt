package com.example.meteochallenge.utils

import android.content.res.Resources
import com.example.meteochallenge.R
import com.example.meteochallenge.models.WeatherState

interface WeatherMapper{
	fun mapToWeatherState(weatherStatus: String): WeatherState
}

class ContextWeatherMapper(resources: Resources) : WeatherMapper {

	val defaultWeatherState = WeatherState("Sunny", R.drawable.sun)

	val weatherState = mapOf(
			"Clear" to WeatherState("Sunny", R.drawable.sun),
			"Drizzle" to WeatherState("Drizzly", R.drawable.drizlle),
			"Rain" to WeatherState("Rainy", R.drawable.rain),
			"Clouds" to WeatherState("Cloudy", R.drawable.cloud),
			"Snow" to WeatherState("Snowy", R.drawable.snow),
			"Storm" to WeatherState("Stormy", R.drawable.storm)
	)

	override fun mapToWeatherState(weatherStatus: String): WeatherState {
		return weatherState[weatherStatus] ?: defaultWeatherState
	}
}


