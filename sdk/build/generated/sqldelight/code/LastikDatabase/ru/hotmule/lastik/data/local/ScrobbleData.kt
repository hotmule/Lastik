package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class ScrobbleData(
  val listenedAt: Long,
  val nowPlaying: Boolean,
  val loved: Boolean?,
  val track: String?,
  val artist: String?,
  val album: String?,
  val lowArtwork: String?
) {
  override fun toString(): String = """
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
