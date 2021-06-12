package com.muster.kt

import org.junit.jupiter.api.Test

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat

import org.http4k.core.*
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus

class RoutesTest {

  private val musterApp = Routes( handleCreateMinistry = { Response(OK).body("an-uuid")}).musterApp

  @Test
  fun shouldCreateAMinistry() {
    val requestBody = """
      {
        "name": "foo",
        "description": "bar"
      }
    """.trimIndent()
    val response = musterApp(Request(POST, "/ministry").body(requestBody))
    assertThat(response, hasStatus(OK).and(hasBody("an-uuid")))
  }
}
