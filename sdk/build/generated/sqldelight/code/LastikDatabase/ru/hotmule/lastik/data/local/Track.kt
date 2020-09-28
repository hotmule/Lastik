package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Track(
  val id: Long,
  val albumId: Long,
  val loved: Boolean,
  val attrsId: Long
) {
  override fun toString(): String = """
  |Track [
  |  id: $id
  |  albumId: $albumId
  |  loved: $loved
  |  attrsId: $attrsId
  |]
  """.trimMargin()
}
