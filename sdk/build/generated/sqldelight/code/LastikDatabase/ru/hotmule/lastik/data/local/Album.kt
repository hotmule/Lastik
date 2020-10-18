package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Album(
  val id: Long,
  val artistId: Long,
  val name: String,
  val lowArtwork: String?,
  val highArtwork: String?,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Album [
  |  id: $id
  |  artistId: $artistId
  |  name: $name
  |  lowArtwork: $lowArtwork
  |  highArtwork: $highArtwork
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
