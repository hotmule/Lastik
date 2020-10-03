package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface ArtistQueries : Transacter {
  fun lastId(): Query<Long>

  fun <T : Any> artistTop(mapper: (
    name: String?,
    rank: Int?,
    playCount: Long?
  ) -> T): Query<T>

  fun artistTop(): Query<ArtistTop>

  fun insert(statId: Long?, name: String?)

  fun deleteArtistTop()
}
