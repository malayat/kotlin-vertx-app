package model

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class SunResult(val results: Results?, val status: String)

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

class SunAdapter {

    companion object {
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("America/Guayaquil"))
    }

    @FromJson
    fun sunFromJson(sunResult: SunResult?): SunModel {
        return SunModel(
            ZonedDateTime.parse(sunResult?.results?.sunrise).format(formatter),
            ZonedDateTime.parse(sunResult?.results?.sunset).format(formatter)
        )
    }
}

data class SunModel(
    val sunrise: String?,
    val sunset: String?
)