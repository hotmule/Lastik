package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface AlbumQueries : Transacter {
  public fun getId(artistId: Long, name: String): Query<Long>

  public fun insert(
    artistId: Long,
    name: String,
    lowArtwork: String?,
    highArtwork: String?
  ): Unit
}
