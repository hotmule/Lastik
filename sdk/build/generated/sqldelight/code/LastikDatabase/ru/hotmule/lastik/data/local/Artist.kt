package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Artist(
  val id: Long,
  val statId: Long?,
  val name: String?
) {
  override fun toString(): String = """
  |Artist [
  |  id: $id
  |  statId: $statId
  |  name: $name
  |]
  """.trimMargin()
}
