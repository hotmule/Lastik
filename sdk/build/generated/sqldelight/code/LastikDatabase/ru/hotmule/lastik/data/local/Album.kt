package ru.hotmule.lastik.`data`.local

import kotlin.Long
import kotlin.String

public data class Album(
  public val id: Long,
  public val artistId: Long,
  public val name: String,
  public val lowArtwork: String?,
  public val highArtwork: String?
) {
  public override fun toString(): String = """
  |Album [
  |  id: $id
  |  artistId: $artistId
  |  name: $name
  |  lowArtwork: $lowArtwork
  |  highArtwork: $highArtwork
  |]
  """.trimMargin()
}
