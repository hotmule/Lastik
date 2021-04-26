package ru.hotmule.lastik.`data`.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class LovedTracks(
  public val artist: String?,
  public val track: String,
  public val loved: Boolean,
  public val lovedAt: Long?,
  public val lowArtwork: String?
) {
  public override fun toString(): String = """
  |LovedTracks [
  |  artist: $artist
  |  track: $track
  |  loved: $loved
  |  lovedAt: $lovedAt
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
