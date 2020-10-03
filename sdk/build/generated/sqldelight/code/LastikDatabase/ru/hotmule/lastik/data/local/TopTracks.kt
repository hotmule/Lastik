package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class TopTracks(
  val artist: String?,
  val track: String?,
  val lowArtwork: String?,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |TopTracks [
  |  artist: $artist
  |  track: $track
  |  lowArtwork: $lowArtwork
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
