package com.example.meteochallenge.core.api

import com.example.meteochallenge.models.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoint {
    @GET("weather")
    fun getWeather(
        @Query("q") country: String,
        @Query("APPID") apiKey: String
    ): Observable<Weather>
}