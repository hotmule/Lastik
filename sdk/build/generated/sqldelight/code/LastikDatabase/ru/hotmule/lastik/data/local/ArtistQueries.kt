package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long
import kotlin.String

interface ArtistQueries : Transacter {
  fun getId(name: String): Query<Long>

  fun insert(name: String)

  fun deleteLovedTracks()
}
