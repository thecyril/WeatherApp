package com.example.meteochallenge.interfaces

import com.example.meteochallenge.core.api.WeatherEndpoint

interface RemoteDataSource {

	fun getWeatherApi(): WeatherEndpoint

}