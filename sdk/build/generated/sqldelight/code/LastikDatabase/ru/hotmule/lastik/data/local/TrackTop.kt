package ru.hotmule.lastik.`data`.local

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class TrackTop(
  public val artist: String?,
  public val track: String?,
  public val rank: Int,
  public val playCount: Long?,
  public val lowArtwork: String?
) {
  public override fun toString(): String = """
  |TrackTop [
  |  artist: $artist
  |  track: $track
  |  rank: $rank
  |  playCount: $playCount
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
