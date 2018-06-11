package verticles

import com.github.kittinunf.fuel.httpGet
import extensions.moshiCustomDeserializerOf
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import model.SunModel
import nl.komponents.kovenant.task
import org.slf4j.LoggerFactory
import uy.klutter.vertx.VertxInit

class MainVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        VertxInit.ensure()
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        val logger = LoggerFactory.getLogger("VertxServer")
        val templateEngine = ThymeleafTemplateEngine.create()

        router.get("/").handler { ctx ->

            val timePromise = task {
                val url = "http://api.sunrise-sunset.org/json?lat=-0.2166667&lng=-78.5&formatted=0"
                val (_, _, result) = url.httpGet().responseObject(moshiCustomDeserializerOf<SunModel>())
                result.component1()
            }

            timePromise.success {
                ctx.put("sunrise", it?.sunrise)
                ctx.put("sunset", it?.sunset)
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