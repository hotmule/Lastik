package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Album(
  val id: Long,
  val artistId: Long,
  val attrsId: Long
) {
  override fun toString(): String = """
  |Album [
  |  id: $id
  |  artistId: $artistId
  |  attrsId: $attrsId
  |]
  """.trimMargin()
}
