package verticles

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import extensions.defaultDeserializerOf
import extensions.sunSetDeserializerOf
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import model.SunModel
import model.SunSetWeatherInfo
import model.WeatherResult
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.functional.map
import nl.komponents.kovenant.task
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.Proxy

class MainVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        val logger = LoggerFactory.getLogger("VertxServer")
        val templateEngine = ThymeleafTemplateEngine.create()

        var addr = InetSocketAddress("2k_uio2.sri.ad", 8002)
        FuelManager.instance.proxy = Proxy(Proxy.Type.HTTP, addr)

        router.get("/").handler { ctx ->

            val sunSetPromise = task {
                val url = "http://api.sunrise-sunset.org/json?lat=-0.2166667&lng=-78.5&formatted=0"
                val (_, _, result) = url.httpGet().responseObject(sunSetDeserializerOf<SunModel>())
                result.component1()
            }

            val weatherPromise = task {
                val url =
                    "http://api.openweathermap.org/data/2.5/weather?id=3651857&units=metric&appid=15646a06818f61f7b8d7823ca833e1ce"
                val (_, _, result) = url.httpGet().responseObject(defaultDeserializerOf<WeatherResult>())
                result.component1()
            }

            val sunsetWeatherInfo = sunSetPromise.bind { sunset ->
                weatherPromise.map { weather ->
                    SunSetWeatherInfo(sunset, weather)
                }
            }

            sunsetWeatherInfo.success {
                ctx.put("sunrise", it.sunsetModel?.sunrise)
                ctx.put("sunset", it.sunsetModel?.sunset)
                ctx.put("temp", it.weatherResult?.main?.temp)
                templateEngine.render(ctx, "public/templates/", "index.html") { buf ->
                    if (buf.failed()) {
                        logger.error("Template rendering failed.", buf.cause())
                    } else {
                        ctx.response().end(buf.result())
                    }
                }
            }
        }

        server.requestHandler {
            router.accept(it)
        }.listen(9090) {
            if (it.succeeded()) {
                logger.info("Listen on port 9090")
            } else {
                logger.error("Failed to listen on port 9090. {}", it.cause().message)
            }
        }
    }
}