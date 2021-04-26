package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface ArtistQueries : Transacter {
  public fun getId(name: String): Query<Long>

  public fun insert(name: String): Unit

  public fun deleteLovedTracks(): Unit
}
