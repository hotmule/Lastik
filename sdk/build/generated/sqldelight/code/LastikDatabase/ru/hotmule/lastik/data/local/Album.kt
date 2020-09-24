package ru.hotmule.lastik.data.local

import kotlin.String

data class Album(
  val id: String,
  val artistId: String,
  val lowResImage: String?,
  val highResImage: String?,
  val name: String?
) {
  override fun toString(): String = """
  |Album [
  |  id: $id
  |  artistId: $artistId
  |  lowResImage: $lowResImage
  |  highResImage: $highResImage
  |  name: $name
  |]
  """.trimMargin()
}
