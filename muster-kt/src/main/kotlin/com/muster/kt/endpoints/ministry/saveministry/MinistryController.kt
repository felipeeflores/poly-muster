package com.muster.kt.endpoints.ministry.saveministry

import org.http4k.core.Response
import org.http4k.core.Status
import java.util.*

fun interface MinistryController {
  fun handleSaveMinistry(createMinistryRequest: CreateMinistryRequest): Response
}

class MinistryControllerImpl(
  private val saveMinistry: (CreateMinistryRequest) -> Optional<UUID>
) : MinistryController {

  override fun handleSaveMinistry(createMinistryRequest: CreateMinistryRequest): Response {
    val ministry = saveMinistry(createMinistryRequest)
    return ministry.map { uuid ->
      Response(Status.OK).body(uuid.toString())
    }.orElse(Response(Status.INTERNAL_SERVER_ERROR).body("unable to fulfil request"))
  }

}
