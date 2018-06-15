package services

import com.github.kittinunf.fuel.httpGet
import extensions.defaultDeserializerOf
import extensions.sunDeserializerOf
import model.SunModel
import model.WeatherResult
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task

object Services {

    fun getSunInfo(lat: Double, lng: Double): Promise<SunModel?, Exception> = task {
        val url = "http://api.sunrise-sunset.org/json?lat=$lat&lng=$lng&formatted=0"
        val (_, _, result) = url.httpGet().responseObject(sunDeserializerOf<SunModel>())
        result.component1()
    }

    fun getWeatherInfo(cityId: Int): Promise<WeatherResult?, Exception> = task {
        val url =
            "http://api.openweathermap.org/data/2.5/weather?id=$cityId&units=metric&appid=15646a06818f61f7b8d7823ca833e1ce"
        val (_, _, result) = url.httpGet().responseObject(defaultDeserializerOf<WeatherResult>())
        result.component1()
    }


}