package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Album(
  val id: Long,
  val nickname: String,
  val artistId: Long,
  val statId: Long?,
  val name: String?,
  val lowArtwork: String?,
  val highArtwork: String?
) {
  override fun toString(): String = """
  |Album [
  |  id: $id
  |  nickname: $nickname
  |  artistId: $artistId
  |  statId: $statId
  |  name: $name
  |  lowArtwork: $lowArtwork
  |  highArtwork: $highArtwork
  |]
  """.trimMargin()
}
