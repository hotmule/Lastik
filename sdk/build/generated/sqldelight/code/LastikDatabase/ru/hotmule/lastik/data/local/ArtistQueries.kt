package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface ArtistQueries : Transacter {
  fun <T : Any> artistData(mapper: (
    name: String?,
    playCount: Long?,
    lowResImage: String?
  ) -> T): Query<T>

  fun artistData(): Query<ArtistData>

  fun lastId(): Query<Long>

  fun insert(attrsId: Long)

  fun deleteAll()
}
