package com.example.meteochallenge.core.repositories

import android.util.Log
import com.example.meteochallenge.BuildConfig
import com.example.meteochallenge.core.api.WeatherDao
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.models.Weather
import com.example.meteochallenge.services.IoServices
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class WeatherRepository(
		private val service: IoServices,
		private val weatherDao: WeatherDao
) : HttpWeatherRepository {

	private val weatherApi = service.getWeatherApi()

	override fun getWeather(city: String): Observable<Weather> {
		refreshUser(city)
		// Returns a LiveData object directly from the database.
		return weatherDao.loadWeatherByCity(city)
	}


	private fun refreshUser(cityName: String) {

		// Runs in a background thread.
			// Check if user data was fetched recently.

		Thread(Runnable {
			Runnable {
				val userExists = weatherDao.hasCity(cityName, FRESH_TIMEOUT)
				Log.d("USER", userExists.toString())
				if (!userExists) {
					// Refreshes the data.
					val response = weatherApi.getWeather(cityName, "metric", BuildConfig.API_KEY)
					Log.d("response", response.toString())
					// Check for errors here.

					// Updates the database. The LiveData object automatically
					// refreshes, so we don't need to do anything else here.
					response.map { weatherDao.save(it) }
				}
			}
		}).start()
	}

	companion object {
		val FRESH_TIMEOUT = TimeUnit.HOURS.toMillis(1)
	}


	// = weatherApi.getWeather(city, "metric", BuildConfig.API_KEY)
}