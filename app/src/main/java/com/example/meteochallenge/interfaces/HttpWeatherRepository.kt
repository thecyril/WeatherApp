package com.example.meteochallenge.interfaces

import com.example.meteochallenge.models.Weather
import io.reactivex.Observable

interface HttpWeatherRepository {

	fun getWeather(): Observable<Weather>
}
