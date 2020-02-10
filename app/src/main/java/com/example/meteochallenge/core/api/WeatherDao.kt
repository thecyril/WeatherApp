package com.example.meteochallenge.core.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.meteochallenge.models.Weather
import io.reactivex.Observable

@Dao
interface WeatherDao {
	@Insert(onConflict = REPLACE)
	fun save(weatherObservable: Weather)

	@Query("SELECT * FROM weather WHERE name = :cityName")
	fun loadWeatherByCity(cityName: String): Observable<Weather>

	@Query("SELECT COUNT(*) FROM weather WHERE name == :cityName AND lastUpdate >= :timeout")
	fun hasCity(cityName: String, timeout: Long): Boolean
}