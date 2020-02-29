package com.example.meteochallenge.core.repositories

import android.icu.util.LocaleData
import com.example.meteochallenge.BuildConfig
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.interfaces.LocaleDataSouce
import com.example.meteochallenge.interfaces.RemoteDataSource
import com.example.meteochallenge.models.Weather
import com.example.meteochallenge.services.IoServices
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.*

class WeatherRepository(
		val remoteDataSource: RemoteDataSource,
		val localeData: LocaleDataSouce
) : HttpWeatherRepository {

	private val weatherApi = remoteDataSource.getWeatherApi()

	override fun getWeather(): Observable<Weather> {

		val dataSourceObserver = weatherApi.getWeather("Paris", "metric", BuildConfig.API_KEY)
				.subscribeOn(Schedulers.io())
				.doOnNext { weather ->
					localeData.setWeatherData(weather)
				}.onErrorReturn {
					localeData.getWeatherData().blockingGet()
				}

		return dataSourceObserver
	}
}