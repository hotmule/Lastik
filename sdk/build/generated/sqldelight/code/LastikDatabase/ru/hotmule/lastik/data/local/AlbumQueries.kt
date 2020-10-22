package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface AlbumQueries : Transacter {
  fun getTopAlbumsCount(): Query<Long>

  fun getId(artistId: Long, name: String): Query<Long>

  fun <T : Any> albumTop(mapper: (
    artist: String?,
    album: String,
    lowArtwork: String?,
    rank: Int?,
    playCount: Long?
  ) -> T): Query<T>

  fun albumTop(): Query<AlbumTop>

  fun insert(
    artistId: Long,
    name: String,
    lowArtwork: String?,
    highArtwork: String?
  )

  fun upsert(
    rank: Int?,
    playCount: Long?,
    artistId: Long,
    name: String,
    lowArtwork: String?,
    highArtwork: String?
  )
}
