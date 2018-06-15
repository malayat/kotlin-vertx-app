package model

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class SunSetResult(val results: Results?, val status: String)

data class Results(
    val sunrise: String?,
    val sunset: String?,
    @Json(name = "solar_noon") val solarNoon: String,
    @Json(name = "day_length") val dayLength: Int,
    @Json(name = "civil_twilight_begin") val civilTwilightBegin: String,
    @Json(name = "civil_twilight_end") val civilTwilightEnd: String,
    @Json(name = "nautical_twilight_begin") val nauticalTwilightBegin: String,
    @Json(name = "nautical_twilight_end") val nauticalTwilightEnd: String,
    @Json(name = "astronomical_twilight_begin") val astronomicalTwilightBegin: String,
    @Json(name = "astronomical_twilight_end") val astronomicalTwilightEnd: String
)


class SunSetAdapter {

    companion object {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("America/Guayaquil"))
    }

    @FromJson
    fun sunSetFromJson(sunSet: SunSetResult?): SunModel {
        return SunModel(
            ZonedDateTime.parse(sunSet?.results?.sunrise).format(formatter),
            ZonedDateTime.parse(sunSet?.results?.sunset).format(formatter)
        )
    }
}

data class SunModel(
    val sunrise: String?,
    val sunset: String?
)