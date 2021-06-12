package com.muster.kt.endpoints.ministry.saveministry

import com.muster.kt.model.Ministry
import java.util.*

class MinistryService(private val getUUID: () -> Optional<UUID>) {
  private var ministryInMemoryStore = mutableMapOf<UUID, Ministry>()

  fun saveMinistry(createMinistryRequest: CreateMinistryRequest): Optional<UUID> {
    return getUUID().map { uuid ->
      val ministry = Ministry(uuid, createMinistryRequest.name, createMinistryRequest.description)
      ministryInMemoryStore.put(uuid, ministry)
      uuid
    }
  }
}
