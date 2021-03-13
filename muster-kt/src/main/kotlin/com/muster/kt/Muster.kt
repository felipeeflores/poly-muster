package com.muster.kt

import com.muster.kt.formats.JacksonMessage
import com.muster.kt.formats.jacksonMessageLens
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import java.time.Duration
import java.util.*
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.filter.DebuggingFilters.PrintResponse
import org.http4k.filter.MicrometerMetrics
import org.http4k.filter.OpenTelemetryMetrics
import org.http4k.filter.OpenTelemetryTracing
import org.http4k.filter.ResilienceFilters.CircuitBreak
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.KtorCIO
import org.http4k.server.asServer

// this is a micrometer registry used mostly for testing - substitute the correct implementation.
val registry = SimpleMeterRegistry()
val circuitBreaker = CircuitBreaker.of("circuit",
        CircuitBreakerConfig.custom()
                .slidingWindow(2, 2, COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofSeconds(1))
                .build()
)

val circuitBreakerEndpointResponses = ArrayDeque<Response>().apply {
    add(Response(OK))
    add(Response(OK))
    add(Response(INTERNAL_SERVER_ERROR))
}

val app = routes(
    "/ping" bind GET to {
        Response(OK).body("pong")
    },

    "/formats/json/jackson" bind GET to {
        Response(OK).with(jacksonMessageLens of JacksonMessage("Barry", "Hello there!"))
    },

    "/testing/hamkrest" bind GET to {request ->
        Response(OK).body("Echo '${request.bodyString()}'")
    },

    "/metrics" bind GET to {
        Response(OK).body("Example metrics route for muster-kt")
    },

    "/resilience" bind GET to {
        circuitBreakerEndpointResponses.pop()
    },

    "/opentelemetrymetrics" bind GET to {
        Response(OK).body("Example metrics route for muster-kt")
    }
)

fun main() {

    val server = PrintRequest()
            .then(ServerFilters.MicrometerMetrics.RequestCounter(registry))
            .then(ServerFilters.MicrometerMetrics.RequestTimer(registry))
            .then(ServerFilters.OpenTelemetryTracing())
            .then(ServerFilters.OpenTelemetryMetrics.RequestCounter())
            .then(ServerFilters.OpenTelemetryMetrics.RequestTimer())
        .then(app)
        .asServer(KtorCIO(9000)).start()

    val client = PrintResponse()
            .then(ClientFilters.MicrometerMetrics.RequestCounter(registry))
            .then(ClientFilters.MicrometerMetrics.RequestTimer(registry))
            .then(ClientFilters.OpenTelemetryTracing())
            .then(ClientFilters.OpenTelemetryMetrics.RequestCounter())
            .then(ClientFilters.OpenTelemetryMetrics.RequestTimer())
            .then(CircuitBreak(circuitBreaker, isError = { r: Response -> !r.status.successful } ))
        .then(OkHttp())

    val response = client(Request(GET, "http://localhost:9000/ping"))

    println(response.bodyString())

    // Example usage of metrics
    // make some calls
    repeat(10) {
        app(Request(GET, "/metrics"))
        client(Request(GET, "https://http4k.org"))
    }
    
    // see some results
    registry.forEachMeter { println("${it.id} ${it.measure().joinToString(",")}") }
    // Resilience4j
    // Example of circuit breaker
    println("Result: " + client(Request(GET, "http://localhost:9000/resilience")).status + " Circuit is: " + circuitBreaker.state)
    println("Result: " + client(Request(GET, "http://localhost:9000/resilience")).status + " Circuit is: " + circuitBreaker.state)
    
    Thread.sleep(1100) // wait for reset
    
    println("Result: " + client(Request(GET, "http://localhost:9000/resilience")).status + " Circuit is: " + circuitBreaker.state)
    println("Result: " + client(Request(GET, "http://localhost:9000/resilience")).status + " Circuit is: " + circuitBreaker.state)
    // Example usage of metrics
    // make some calls
    repeat(10) {
        app(Request(GET, "/opentelemetrymetrics"))
        client(Request(GET, "https://http4k.org"))
    }

    println("Server started on " + server.port())
}
