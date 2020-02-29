package com.example.meteochallenge.interfaces

import com.example.meteochallenge.models.Weather
import io.reactivex.Maybe

interface LocaleDataSouce {

	fun getWeatherData(): Maybe<Weather>

	fun setWeatherData(weather: Weather)
}
