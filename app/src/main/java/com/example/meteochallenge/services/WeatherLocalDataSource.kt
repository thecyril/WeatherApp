package com.example.meteochallenge.services

import android.content.Context
import android.content.SharedPreferences
import com.example.meteochallenge.interfaces.LocaleDataSouce
import com.example.meteochallenge.models.Weather
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class WeatherLocalDataSource(val context: Context): LocaleDataSouce {

	private val prefs: SharedPreferences by lazy { context.getSharedPreferences("LOCAL_PREF", Context.MODE_PRIVATE) }

	override fun getWeatherData(): Maybe<Weather> {
		return Maybe.fromCallable {
			Gson().fromJson(prefs.getString("WEATHER_DATA", "{}"), Weather::class.java)
		}.subscribeOn(Schedulers.io())
	}

	override fun setWeatherData(weather: Weather) {
		prefs.edit().putString("WEATHER_DATA", Gson().toJson(weather)).apply()
	}
}