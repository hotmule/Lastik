package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface ArtistQueries : Transacter {
  fun getTopArtistsCount(): Query<Long>

  fun lastId(): Query<Long>

  fun <T : Any> artistTop(userName: String, mapper: (
    name: String,
    rank: Int?,
    playCount: Long?
  ) -> T): Query<T>

  fun artistTop(userName: String): Query<ArtistTop>

  fun insert(
    userName: String,
    name: String,
    rank: Int?,
    playCount: Long?
  )

  fun deleteScrobbles(userName: String)

  fun deleteTopArtist(userName: String)

  fun deleteTopAlbums(userName: String)

  fun deleteTopTracks(userName: String)

  fun deleteLovedTracks(userName: String)
}
