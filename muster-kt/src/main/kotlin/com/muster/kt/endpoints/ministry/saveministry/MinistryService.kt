package com.muster.kt.endpoints.ministry.saveministry

import com.muster.kt.model.Ministry
import java.util.*

class MinistryService(private val getUUID: () -> Optional<UUID>) {
  private val ministryInMemoryStore = mutableMapOf<UUID, Ministry>()

  fun saveMinistry(createMinistryRequest: CreateMinistryRequest): Optional<UUID> {
    return getUUID().flatMap { uuid ->
      val ministry = Ministry(uuid, createMinistryRequest.name, createMinistryRequest.description)
      ministryInMemoryStore[uuid] = ministry
      Optional.ofNullable(ministryInMemoryStore[uuid]).map { m -> m.id }
    }
  }
}
