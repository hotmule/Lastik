package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface ScrobbleQueries : Transacter {
  public fun getScrobblesCount(): Query<Long>

  public fun <T : Any> scrobbleData(mapper: (
    listenedAt: Long,
    nowPlaying: Boolean,
    loved: Boolean?,
    track: String?,
    artist: String?,
    album: String?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun scrobbleData(): Query<ScrobbleData>

  public fun insert(
    trackId: Long,
    listenedAt: Long,
    nowPlaying: Boolean
  ): Unit

  public fun deleteAll(): Unit
}
