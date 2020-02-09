package com.example.meteochallenge

import android.app.Application
import com.example.meteochallenge.core.repositories.WeatherRepository
import com.example.meteochallenge.core.translator.ContextTranslator
import com.example.meteochallenge.core.translator.Translator
import com.example.meteochallenge.interfaces.HttpWeatherRepository
import com.example.meteochallenge.services.IoServices
import com.example.meteochallenge.utils.ContextWeatherMapper
import com.example.meteochallenge.utils.WeatherMapper
import com.example.meteochallenge.viewmodels.WeatherViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App: Application() {

	val retrofitModule = module {

		fun provideHttpClient(): OkHttpClient {
			val okHttpClientBuilder = OkHttpClient.Builder()

			return okHttpClientBuilder.build()
		}

		fun provideRetrofit(client: OkHttpClient): Retrofit {
			return Retrofit.Builder()
					.baseUrl(BuildConfig.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.client(client)
					.build()
		}

		single { provideHttpClient() }
		single { provideRetrofit(client = get()) }
	}

	val serviceModule = module {
		single { IoServices(retrofit = get()) }
	}

	val translatorModule = module {
		single<Translator> { ContextTranslator(androidContext()) }
	}

	val weatherModule = module {
		single<HttpWeatherRepository> { WeatherRepository(service = get()) }
		single<WeatherMapper> { ContextWeatherMapper(androidContext().resources) }

		viewModel {
			WeatherViewModel(weatherRepository = get(), translator = get(), weatherMapper = get())
		}
	}

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@App)
			printLogger(Level.DEBUG)
			modules(
					listOf(
							translatorModule,
							retrofitModule,
							serviceModule,
							weatherModule
					)
			)
		}
	}
}