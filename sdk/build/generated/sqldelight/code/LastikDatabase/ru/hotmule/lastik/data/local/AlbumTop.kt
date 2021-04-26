package ru.hotmule.lastik.`data`.local

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class AlbumTop(
  public val artist: String?,
  public val album: String?,
  public val lowArtwork: String?,
  public val rank: Int,
  public val playCount: Long?
) {
  public override fun toString(): String = """
  |AlbumTop [
  |  artist: $artist
  |  album: $album
  |  lowArtwork: $lowArtwork
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
