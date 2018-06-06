import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    val server = vertx.createHttpServer()
    val router = Router.router(vertx)
    val logger = LoggerFactory.getLogger("VertxServer")

    router.get("/").handler {
        it.response().end("Hello Vertx and Router with Kotlin")
    }

    server.requestHandler {
        router.accept(it)
    }.listen(9090) {
        if (!it.succeeded()) {
            logger.error("Failed to listen on port 9090. {}", it.cause().message)
        }
    }
}