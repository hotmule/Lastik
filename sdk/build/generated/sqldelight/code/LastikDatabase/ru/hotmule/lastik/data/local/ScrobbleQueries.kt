package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String

interface ScrobbleQueries : Transacter {
  fun getScrobblesCount(): Query<Long>

  fun <T : Any> scrobbleData(mapper: (
    listenedAt: Long,
    nowPlaying: Boolean,
    loved: Boolean?,
    track: String?,
    artist: String?,
    album: String?,
    lowArtwork: String?
  ) -> T): Query<T>

  fun scrobbleData(): Query<ScrobbleData>

  fun insert(
    trackId: Long,
    listenedAt: Long,
    nowPlaying: Boolean
  )

  fun deleteAll()
}
