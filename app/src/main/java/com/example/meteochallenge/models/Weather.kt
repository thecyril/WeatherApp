package com.example.meteochallenge.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Weather(
		var base: String? = null,
		@Embedded
		@Ignore var clouds: Clouds? = null,
		var cod: Int = 0,
		@Embedded
		@Ignore var coord: Coord? = null,
		var dt: Int = 0,
		var id: Int = 0,
		@Embedded
		var main: Main?,
		@PrimaryKey var name: String,
		@Embedded
		@Ignore var sys: Sys?,
		var timezone: Int = 0,
		var visibility: Int = 0,
		@Embedded
		@Ignore var weather: List<WeatherX>?,
		@Embedded
		var wind: Wind?,
		var lastUpdate: Long? = 0
) {
	constructor() : this("null", null, 0, null, 0, 0,
			null, "", null, 0, 0, null, null, 0) {

	}
}

data class Clouds(
		var all: Int
)

data class Coord(
		var lat: Double,
		var lon: Double
)

data class Main(
		@SerializedName("feels_like")
		var feelsLike: Double,
		var humidity: Int,
		var pressure: Int,
		var temp: Double,
		@SerializedName("temp_max")
		var tempMax: Double,
		@SerializedName("temp_min")
		var tempMin: Double
)

data class Sys(
		var country: String,
		var id: Int,
		var sunrise: Int,
		var sunset: Int,
		var type: Int
)

data class WeatherX(
		var description: String,
		var icon: String,
		var id: Int,
		var main: String
)

data class Wind(
		var speed: Double
)
