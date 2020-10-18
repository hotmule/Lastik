package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

data class Track(
  val id: Long,
  val artistId: Long,
  val albumId: Long?,
  val name: String,
  val loved: Boolean,
  val lovedAt: Long?,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Track [
  |  id: $id
  |  artistId: $artistId
  |  albumId: $albumId
  |  name: $name
  |  loved: $loved
  |  lovedAt: $lovedAt
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
