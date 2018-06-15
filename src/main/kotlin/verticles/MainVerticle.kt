package verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import model.SunWeatherInfo
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.functional.map
import org.slf4j.LoggerFactory
import services.Services

class MainVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        val logger = LoggerFactory.getLogger("VertxServer")
        val templateEngine = ThymeleafTemplateEngine.create()

        router.get("/").handler { ctx ->

            val sunPromise = Services.getSunInfo(-0.2166667, -78.5)

            val weatherPromise = Services.getWeatherInfo(3651857)

            val sunWeatherInfo = sunPromise.bind { sun ->
                weatherPromise.map { weather ->
                    SunWeatherInfo(sun, weather)
                }
            }

            sunWeatherInfo.success {
                ctx.put("sunrise", it.sunModel?.sunrise)
                ctx.put("sunset", it.sunModel?.sunset)
                ctx.put("temp", it.weatherResult?.main?.temp)
                templateEngine.render(ctx, "public/templates/", "index.html") { buf ->
                    if (buf.failed()) {
                        logger.error("Template rendering failed.", buf.cause())
                    } else {
                        ctx.response().end(buf.result())
                    }
                }
            }

            sunWeatherInfo.fail {
                logger.error("Remote server querying failed", it)

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