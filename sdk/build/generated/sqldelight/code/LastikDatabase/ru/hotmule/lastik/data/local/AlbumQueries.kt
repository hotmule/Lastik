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
    lowArtwork: String?,
    rank: Int?,
    playCount: Long?
  ) -> T): Query<T>

  fun albumTop(): Query<AlbumTop>

  fun insert(
    artistId: Long,
    statId: Long?,
    name: String?,
    lowArtwork: String?,
    highArtwork: String?
  )

  fun deleteAlbumTop()
}
