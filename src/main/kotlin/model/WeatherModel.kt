package model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResult(
    val main: Main
)

data class Main(
    val temp: Double,
    val pressure: Long,
    val humidity: Int,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_mas") val tempMax: Double
)