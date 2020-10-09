package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class LovedTracks(
  val artist: String?,
  val track: String?,
  val loved: Boolean,
  val lovedAt: Long?,
  val lowArtwork: String?
) {
  override fun toString(): String = """
  |LovedTracks [
  |  artist: $artist
  |  track: $track
  |  loved: $loved
  |  lovedAt: $lovedAt
  |  lowArtwork: $lowArtwork
  |]
  """.trimMargin()
}
