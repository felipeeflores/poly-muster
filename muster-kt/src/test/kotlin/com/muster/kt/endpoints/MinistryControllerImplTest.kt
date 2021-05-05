package com.muster.kt.endpoints


import com.muster.kt.model.Ministry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.util.*

internal class MinistryControllerImplTest {

  private val testUUID = UUID.fromString("459a1eb6-ad8d-11eb-8529-0242ac130003")
  private val controller = MinistryControllerImpl(
    getUUID = { testUUID },
    saveMinistry = { m: Ministry -> Optional.of(m.id)}
  )

  @Test
  fun handleSaveMinistry_shouldSaveMinistry() {
    val cmr = CreateMinistryRequest("Foo", "Bar")
    assertThat(controller.handleSaveMinistry(cmr), equalTo(Optional.of(testUUID)))
  }
}
