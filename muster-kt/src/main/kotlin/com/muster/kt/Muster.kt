package com.muster.kt

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.filter.MicrometerMetrics
import org.http4k.filter.OpenTelemetryMetrics
import org.http4k.filter.OpenTelemetryTracing
import org.http4k.filter.ServerFilters
import org.http4k.server.KtorCIO
import org.http4k.server.asServer

// this is a micrometer registry used mostly for testing - substitute the correct implementation.
val registry = SimpleMeterRegistry()

val app = Runtime.application

fun main() {

  PrintRequest()
    .then(ServerFilters.MicrometerMetrics.RequestCounter(registry))
    .then(ServerFilters.MicrometerMetrics.RequestTimer(registry))
    .then(ServerFilters.OpenTelemetryTracing())
    .then(ServerFilters.OpenTelemetryMetrics.RequestCounter())
    .then(ServerFilters.OpenTelemetryMetrics.RequestTimer())
    .then(app)
    .asServer(KtorCIO(8080))
    .start()
}
