package ru.hotmule.lastik.`data`.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class ScrobbleData(
  public val listenedAt: Long,
  public val nowPlaying: Boolean,
  public val loved: Boolean?,
  public val track: String?,
  public val artist: String?,
  public val album: String?,
  public val lowArtwork: String?
) {
  public override fun toString(): String = """
  |ScrobbleData [
  |  listenedAt: $listenedAt
  |  nowPlaying: $nowPlaying
  |  loved: $loved
  |  track: $track
  |  artist: $artist
  |  album: $album
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
