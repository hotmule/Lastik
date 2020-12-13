package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class AlbumTop(
  val artist: String?,
  val album: String,
  val lowArtwork: String?,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |AlbumTop [
  |  artist: $artist
  |  album: $album
  |  lowArtwork: $lowArtwork
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
