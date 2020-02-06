package com.example.meteochallenge.views.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.meteochallenge.R
import com.example.meteochallenge.viewmodels.WeatherViewModel
import com.example.meteochallenge.views.adapters.WeatherAdapter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject

class WeatherActivity : AppCompatActivity() {

	private val viewModel: WeatherViewModel by inject()

	private lateinit var hubStoryAdapter: WeatherAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		viewModel.onViewCreated()
		bindViewModel()
	}

	private fun bindViewModel() {
		val weatherDisposable = viewModel
				.getWeatherObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {event ->
					when (event) {
						is WeatherViewModel.ViewState.Loading -> {
							Log.d("DATA", "HERE")
							// TODO: Handle Loading
						}
						is WeatherViewModel.ViewState.Empty -> {
							// TODO: Handle Empty
						}
						is WeatherViewModel.ViewState.Error -> {
							Log.e("Api Error", event.error)
						}
						is WeatherViewModel.ViewState.Success -> {
							Log.d("DATA", event.output.toString())
							//handleSuccess(event.output)
						}
					}
				}
	}

	private fun handleSuccess(output: WeatherViewModel.Output) {
		hubStoryAdapter.setData(output.weather)
		hubStoryAdapter.notifyDataSetChanged()
	}
}
