package com.example.meteochallenge.viewmodels

import androidx.lifecycle.ViewModel
import com.example.meteochallenge.R
import com.example.meteochallenge.core.translator.Translator
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.models.Weather
import com.example.meteochallenge.models.WeatherData
import com.example.meteochallenge.utils.WeatherMapper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class WeatherViewModel(
		private val weatherRepository: HttpWeatherRepository,
		private val translator: Translator,
		private val weatherMapper: WeatherMapper
) : ViewModel() {

	private val weatherSubject = BehaviorSubject.create<Weather>()
	private val errorSubject = BehaviorSubject.create<String>()
	private val loadingSubject = PublishSubject.create<Unit>()

	private val disposables = CompositeDisposable()

	data class Output(
			val weatherData: WeatherData
	)

	sealed class ViewState {
		object Loading : ViewState()
		object Empty : ViewState()
		class Error(val error: String) : ViewState()
		class Success(val output: Output) : ViewState()
	}

	fun onViewCreated() {
		observeWeatherRepository()
		loadingSubject.onNext(Unit)
	}

	fun observeWeatherRepository() {
		val weatherObservable = weatherRepository
				.getWeather()
				.delay(10, TimeUnit.SECONDS)
				.repeat()
				.retry()
				.subscribeOn(Schedulers.io())
				.subscribeBy(
						onError = {
							errorSubject.onNext(it.toString())
						},
						onNext = {
							weatherSubject.onNext(it)
						}
				)
		disposables.add(weatherObservable)
	}

	private fun createWeatherData(weather: Weather, weatherMapper: WeatherMapper): WeatherData {

		val temp = weather.main.temp.roundToInt().toString() + translator.getString(R.string.degre_celcius)
		val pressure = weather.main.pressure.toString()
		val tempMinMax = translator.getString(R.string.temp_min_max, weather.main.tempMin, weather.main.tempMax)
		val windSpeed = weather.wind.speed.toString() + translator.getString(R.string.metric_speed)
		val humidity = weather.main.humidity.toString() + "%"


		return WeatherData(
				temp,
				WeatherDataViewModel(
						weatherMapper.mapToWeatherState(weather.weather[0].main),
						pressure,
						tempMinMax,
						windSpeed,
						humidity
				)
		)
	}

	fun getWeatherObservable(): Observable<ViewState> {

		return Observable.merge(weatherSubject, errorSubject, loadingSubject).map {
			when (it) {
				is Weather -> ViewState.Success(Output(createWeatherData(it, weatherMapper)))
				is String -> ViewState.Error(it)
				else -> ViewState.Loading
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		disposables.clear()
	}
}