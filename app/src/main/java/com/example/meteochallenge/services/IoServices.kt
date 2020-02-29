package com.example.meteochallenge.services

import com.example.meteochallenge.core.api.WeatherEndpoint
import com.example.meteochallenge.interfaces.RemoteDataSource
import retrofit2.Retrofit

class IoServices(private val retrofit: Retrofit): RemoteDataSource {

	override fun getWeatherApi(): WeatherEndpoint {
		return retrofit.create(WeatherEndpoint::class.java)
	}
}