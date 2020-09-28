package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.String

data class ScrobbleData(
  val date: String?,
  val nowPlaying: Boolean,
  val loved: Boolean?,
  val track: String?,
  val artist: String?,
  val album: String?,
  val lowResImage: String?
) {
  override fun toString(): String = """
  |ScrobbleData [
  |  date: $date
  |  nowPlaying: $nowPlaying
  |  loved: $loved
  |  track: $track
  |  artist: $artist
  |  album: $album
  |  lowResImage: $lowResImage
  |]
  """.trimMargin()
}
