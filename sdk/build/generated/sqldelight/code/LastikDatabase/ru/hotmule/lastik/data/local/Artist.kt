package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Artist(
  val id: Long,
  val userName: String,
  val statId: Long?,
  val name: String?
) {
  override fun toString(): String = """
  |Artist [
  |  id: $id
  |  userName: $userName
  |  statId: $statId
  |  name: $name
  |]
  """.trimMargin()
}
