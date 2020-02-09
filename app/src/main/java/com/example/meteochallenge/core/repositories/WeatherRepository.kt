package com.example.meteochallenge.core.repositories

import com.example.meteochallenge.BuildConfig
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.services.IoServices

class WeatherRepository(service: IoServices): HttpWeatherRepository {

	private val weatherApi = service.getWeatherApi()

	override fun getWeather() = weatherApi.getWeather("Paris", "metric", BuildConfig.API_KEY)
}