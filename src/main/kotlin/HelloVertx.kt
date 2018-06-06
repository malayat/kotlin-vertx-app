import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    val server = vertx.createHttpServer()
    val router = Router.router(vertx)
    val logger = LoggerFactory.getLogger("VertxServer")
    val templateEngine = ThymeleafTemplateEngine.create()

    router.get("/").handler { ctx ->
        ctx.put("time", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
        templateEngine.render(ctx, "public/templates/", "index.html") { buf ->
            if (buf.failed()) {
                logger.error("Template rendering failed.", buf.cause())
            } else {
                ctx.response().end(buf.result())
            }
        }
    }

    server.requestHandler {
        router.accept(it)
    }.listen(9090) {
        if (!it.succeeded()) {
            logger.error("Failed to listen on port 9090. {}", it.cause().message)
        }
    }
}