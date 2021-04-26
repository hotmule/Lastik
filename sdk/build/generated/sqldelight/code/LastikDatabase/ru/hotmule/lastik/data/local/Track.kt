package ru.hotmule.lastik.`data`.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class Track(
  public val id: Long,
  public val artistId: Long,
  public val albumId: Long?,
  public val name: String,
  public val loved: Boolean,
  public val lovedAt: Long?
) {
  public override fun toString(): String = """
  |Track [
  |  id: $id
  |  artistId: $artistId
  |  albumId: $albumId
  |  name: $name
  |  loved: $loved
  |  lovedAt: $lovedAt
  |]
  """.trimMargin()
}
