package com.muster.kt.endpoints.ministry.saveministry

import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Status
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test
import java.util.*

internal class MinistryControllerImplTest {

  private val testUUID = Optional.of(UUID.fromString("459a1eb6-ad8d-11eb-8529-0242ac130003"))
  private val controller = MinistryControllerImpl(
    saveMinistry = { m: CreateMinistryRequest ->
      if (m.name == "Foo") testUUID else Optional.empty()
    }
  )

  @Test
  fun handleSaveMinistry_shouldSaveMinistry() {
    val cmr = CreateMinistryRequest("Foo", "Bar")
    assertThat(controller.handleSaveMinistry(cmr), hasStatus(Status.OK))
  }

  @Test
  fun handleSaveMinistry_shouldReturnBodyWithId() {
    val cmr = CreateMinistryRequest("Foo", "Bar")
    assertThat(controller.handleSaveMinistry(cmr), hasBody("459a1eb6-ad8d-11eb-8529-0242ac130003"))
  }

  @Test
  fun handleSaveMinistry_shouldFailSavingWith500() {
    val cmr = CreateMinistryRequest("Baz", "Blah")
    assertThat(controller.handleSaveMinistry(cmr), hasStatus(Status.INTERNAL_SERVER_ERROR))
  }
}
