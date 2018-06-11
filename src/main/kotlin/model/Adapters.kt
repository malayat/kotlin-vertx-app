package model

import com.squareup.moshi.FromJson
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SunAdapter {

    companion object {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("America/Guayaquil"))
    }

    @FromJson
    fun sunFromJson(sunJson: SunJson): SunModel {
        return SunModel(
            ZonedDateTime.parse(sunJson.results?.sunrise).format(formatter),
            ZonedDateTime.parse(sunJson.results?.sunset).format(formatter)
        )
    }

}