package ru.hotmule.lastik.`data`.local

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class ArtistTop(
  public val name: String?,
  public val rank: Int,
  public val playCount: Long?,
  public val lowArtwork: String?
) {
  public override fun toString(): String = """
  |ArtistTop [
  |  name: $name
  |  rank: $rank
  |  playCount: $playCount
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
