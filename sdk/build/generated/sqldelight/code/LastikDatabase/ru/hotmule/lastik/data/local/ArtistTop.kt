package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class ArtistTop(
  val name: String?,
  val rank: Int,
  val playCount: Long?,
  val lowArtwork: String?
) {
  override fun toString(): String = """
  |ArtistTop [
  |  name: $name
  |  rank: $rank
  |  playCount: $playCount
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
