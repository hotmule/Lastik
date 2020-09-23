package ru.hotmule.lastik.data.local

import kotlin.String

data class Track(
  val name: String?,
  val artist: String?,
  val album: String?,
  val image: String?
) {
  override fun toString(): String = """
  |Track [
  |  name: $name
  |  artist: $artist
  |  album: $album
  |  image: $image
  |]
  """.trimMargin()
}
