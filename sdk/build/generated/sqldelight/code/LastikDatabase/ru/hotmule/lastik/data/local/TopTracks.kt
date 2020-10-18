package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class TopTracks(
  val artist: String?,
  val track: String,
  val rank: Int?,
  val playCount: Long?,
  val lowArtwork: String?
) {
  override fun toString(): String = """
  |TopTracks [
  |  artist: $artist
  |  track: $track
  |  rank: $rank
  |  playCount: $playCount
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
