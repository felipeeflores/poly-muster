package com.muster.kt

import com.muster.kt.endpoints.MinistryControllerImpl
import com.muster.kt.endpoints.MinistryService
import com.muster.kt.model.Ministry
import java.util.*

object Runtime {
  private object MinistryServiceRuntime {
    val ministrService = MinistryService(
      getUUID = { Optional.ofNullable(UUID.randomUUID()) }
    )
    val ministryController = MinistryControllerImpl(
      saveMinistry = { cmr -> ministrService.saveMinistry(cmr) }
    )
  }
  val application = Routes(
    handleCreateMinistry = { request -> MinistryServiceRuntime.ministryController.handleSaveMinistry(request) }
  ).musterApp
}
