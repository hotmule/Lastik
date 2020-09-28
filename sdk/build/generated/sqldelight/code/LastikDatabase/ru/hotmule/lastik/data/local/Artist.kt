package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Artist(
  val id: Long,
  val attrsId: Long
) {
  override fun toString(): String = """
  |Artist [
  |  id: $id
  |  attrsId: $attrsId
  |]
  """.trimMargin()
}
