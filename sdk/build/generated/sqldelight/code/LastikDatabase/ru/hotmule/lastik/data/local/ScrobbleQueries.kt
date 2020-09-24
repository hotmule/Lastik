package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.String

interface ScrobbleQueries : Transacter {
  fun <T : Any> selectAll(mapper: (
    id: String,
    artistId: String,
    albumId: String,
    name: String?,
    loved: Boolean
  ) -> T): Query<T>

  fun selectAll(): Query<Track>

  fun insert(scrobble: Scrobble)
}
