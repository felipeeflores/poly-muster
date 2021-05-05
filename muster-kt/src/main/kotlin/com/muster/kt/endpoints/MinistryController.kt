package com.muster.kt.endpoints

import com.muster.kt.model.Ministry
import java.util.*

fun interface MinistryController {
  fun handleSaveMinistry(ministry: CreateMinistryRequest): Optional<UUID>
}

class MinistryControllerImpl(
  private val getUUID: () -> UUID,
  private val saveMinistry: (Ministry) -> Optional<UUID>
) : MinistryController {
  override fun handleSaveMinistry(createMinistryRequest: CreateMinistryRequest): Optional<UUID> {
    return saveMinistry(
      Ministry(
        id = getUUID(),
        name = createMinistryRequest.name,
        description = createMinistryRequest.description
      )
    )
  }
}

