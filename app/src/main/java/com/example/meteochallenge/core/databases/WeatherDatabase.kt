package com.example.meteochallenge.core.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.meteochallenge.core.api.WeatherDao
import com.example.meteochallenge.models.Weather

@Database(entities = [Weather::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
	abstract fun weatherDao(): WeatherDao
}