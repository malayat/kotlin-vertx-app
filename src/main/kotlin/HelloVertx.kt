import io.vertx.core.Vertx

fun main(args: Array<String>) {
    val server = Vertx.vertx().createHttpServer()
    server.requestHandler {
        it.response().end("Hello Vertx with Kotlin!")
    }.listen(9090) {
        if (!it.succeeded()) println("Failed to listen on port 9090")
    }
}