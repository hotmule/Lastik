package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface ArtistQueries : Transacter {
  fun getTopArtistsCount(): Query<Long>

  fun getId(name: String): Query<Long>

  fun <T : Any> artistTop(mapper: (
    name: String,
    rank: Int?,
    playCount: Long?
  ) -> T): Query<T>

  fun artistTop(): Query<ArtistTop>

  fun insert(name: String)

  fun deleteScrobbles()

  fun deleteTopArtist()

  fun deleteTopAlbums()

  fun deleteTopTracks()

  fun deleteLovedTracks()

  fun upsert(
    rank: Int?,
    playCount: Long?,
    name: String
  )
}
