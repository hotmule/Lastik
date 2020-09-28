package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class ArtistData(
  val name: String?,
  val playCount: Long?,
  val lowResImage: String?
) {
  override fun toString(): String = """
  |ArtistData [
  |  name: $name
  |  playCount: $playCount
  |  lowResImage: $lowResImage
  |]
  """.trimMargin()
}
