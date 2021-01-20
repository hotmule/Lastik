package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class TrackTop(
  val artist: String?,
  val track: String?,
  val rank: Int,
  val playCount: Long?,
  val lowArtwork: String?
) {
  override fun toString(): String = """
  |TrackTop [
  |  artist: $artist
  |  track: $track
  |  rank: $rank
  |  playCount: $playCount
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
