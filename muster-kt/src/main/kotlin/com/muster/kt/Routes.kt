package com.muster.kt

import com.muster.kt.endpoints.CreateMinistryRequest
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.routes

class Routes(private val handleCreateMinistry: (CreateMinistryRequest) -> Response) {
  private val createMinistryLens = Body.auto<CreateMinistryRequest>().toLens()

  val musterApp = routes(
    "/ministry" bind POST to { request: Request -> handleCreateMinistry(createMinistryLens(request)) },
  )
}
