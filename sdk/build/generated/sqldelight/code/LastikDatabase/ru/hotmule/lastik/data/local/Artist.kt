package ru.hotmule.lastik.data.local

import kotlin.String

data class Artist(
  val id: String,
  val name: String?
) {
  override fun toString(): String = """
  |Artist [
  |  id: $id
  |  name: $name
  |]
  """.trimMargin()
}
