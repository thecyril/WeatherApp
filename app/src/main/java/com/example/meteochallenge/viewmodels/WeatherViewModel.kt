package com.example.meteochallenge.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.models.Weather
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ScheduledThreadPoolExecutor

class WeatherViewModel(
		private val weatherRepository: HttpWeatherRepository
) : ViewModel() {

	private val weatherSubject = BehaviorSubject.create<Weather>()
	private val errorSubject = BehaviorSubject.create<String>()
	private val loadingSubject = PublishSubject.create<Unit>()

	data class Output(
			val weather: Weather
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
				.subscribeOn(Schedulers.io())
				.subscribeBy(
						onError = {
							errorSubject.onNext(it.toString())
						},
						onNext = {
							weatherSubject.onNext(it)
						}
				)
	}

	fun getWeatherObservable(): Observable<ViewState> {

		return Observable.merge(weatherSubject, errorSubject, loadingSubject).map {
			when (it) {
				is Weather -> ViewState.Success(Output(it))
				is String -> ViewState.Error(it)
				else -> ViewState.Loading
			}
		}
	}
}