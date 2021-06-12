package com.muster.kt.endpoints.ministry.saveministry

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.util.*

internal class MinistryServiceTest {

  private val testUUID = Optional.of(UUID.fromString("459a1eb6-ad8d-11eb-8529-0242ac130003"))
  private val service = MinistryService(getUUID = { testUUID })

  @Test
  fun shouldSaveMinistry() {
    val cmr = CreateMinistryRequest("Foo", "Bar")
    assertThat(service.saveMinistry(cmr), equalTo(testUUID))
  }
}
