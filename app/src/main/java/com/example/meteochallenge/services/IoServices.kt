package com.example.meteochallenge.services

import com.example.meteochallenge.core.api.WeatherEndpoint
import retrofit2.Retrofit

class IoServices(private val retrofit: Retrofit) {

	fun getWeatherApi(): WeatherEndpoint {
		return retrofit.create(WeatherEndpoint::class.java)
	}
}