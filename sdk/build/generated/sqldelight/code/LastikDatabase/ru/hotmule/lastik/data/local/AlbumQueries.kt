package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface AlbumQueries : Transacter {
  fun lastId(): Query<Long>

  fun <T : Any> albumTop(mapper: (
    artist: String?,
    album: String?,
    rank: Int?,
    playCount: Int?,
    lowResImage: String?
  ) -> T): Query<T>

  fun albumTop(): Query<AlbumTop>

  fun insert(artistId: Long, attrsId: Long)

  fun deleteAlbumTop()
}
