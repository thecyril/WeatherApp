package com.example.meteochallenge.views.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meteochallenge.R
import com.example.meteochallenge.viewmodels.WeatherViewModel
import com.example.meteochallenge.views.adapters.WeatherAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class WeatherActivity : AppCompatActivity() {

	private val viewModel: WeatherViewModel by inject()

	private lateinit var weatherAdapter: WeatherAdapter
	private lateinit var weatherRecyclerView: RecyclerView
	private lateinit var weatherTemperature: TextView

	private val disposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		viewModel.onViewCreated()

		bindView()
		bindViewModel()
		configRecyclerView()
	}

	private fun bindView() {
		weatherRecyclerView = data_recyclerview
		weatherTemperature = weather_temperature
	}


	private fun bindViewModel() {
		val weatherDisposable = viewModel
				.getWeatherObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { event ->
					when (event) {
						is WeatherViewModel.ViewState.Loading -> {
							Log.d("DATA", "LOADING")
							// TODO: Handle Loading
						}
						is WeatherViewModel.ViewState.Empty -> {
							// TODO: Handle Empty
						}
						is WeatherViewModel.ViewState.Error -> {
							Log.e("Api Error", event.error)
						}
						is WeatherViewModel.ViewState.Success -> {
							Log.d("Success", event.output.toString())
							handleSuccess(event.output)
						}
					}
				}
		disposables.add(weatherDisposable)
	}

	private fun handleSuccess(output: WeatherViewModel.Output) {
		weatherAdapter.data = output.weatherData.weatherViewModel
		weatherAdapter.notifyDataSetChanged()
		weatherTemperature.text = output.weatherData.temperature
	}

	private fun configRecyclerView() {
		val gridlayoutManager = LinearLayoutManager(this)

		weatherRecyclerView.layoutManager = gridlayoutManager
		weatherAdapter = WeatherAdapter(this)
		weatherRecyclerView.adapter = weatherAdapter
	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.clear()
	}
}
