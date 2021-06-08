package com.muster.kt.endpoints

import com.muster.kt.model.Ministry
import org.http4k.core.Response
import org.http4k.core.Status
import java.util.*

fun interface MinistryController {
  fun handleSaveMinistry(ministry: CreateMinistryRequest): Response
}

class MinistryControllerImpl(
  private val getUUID: () -> UUID,
  private val saveMinistry: (Ministry) -> Optional<UUID>
) : MinistryController {
  override fun handleSaveMinistry(createMinistryRequest: CreateMinistryRequest): Response {
    val result = saveMinistry(
      Ministry(
        id = getUUID(),
        name = createMinistryRequest.name,
        description = createMinistryRequest.description
      )
    )

    return result.map { uuid ->
      Response(Status.OK).body(uuid.toString())
    }.orElse(Response(Status.INTERNAL_SERVER_ERROR).body("unable to fulfil request"))
  }
}

