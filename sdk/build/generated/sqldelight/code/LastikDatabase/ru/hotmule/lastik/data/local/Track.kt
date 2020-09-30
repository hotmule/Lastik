package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Track(
  val id: Long,
  val artistId: Long,
  val albumId: Long?,
  val statId: Long?,
  val name: String?,
  val loved: Boolean
) {
  override fun toString(): String = """
  |Track [
  |  id: $id
  |  artistId: $artistId
  |  albumId: $albumId
  |  statId: $statId
  |  name: $name
  |  loved: $loved
  |]
  """.trimMargin()
}
