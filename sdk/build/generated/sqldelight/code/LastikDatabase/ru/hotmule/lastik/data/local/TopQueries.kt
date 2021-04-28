package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface TopQueries : Transacter {
  public fun getTopCount(type: Long, period: Long): Query<Long>

  public fun <T : Any> artistTop(period: Long, mapper: (
    name: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun artistTop(period: Long): Query<ArtistTop>

  public fun <T : Any> albumTop(period: Long, mapper: (
    artist: String?,
    album: String?,
    lowArtwork: String?,
    rank: Int,
    playCount: Long?
  ) -> T): Query<T>

  public fun albumTop(period: Long): Query<AlbumTop>

  public fun <T : Any> trackTop(period: Long, mapper: (
    artist: String?,
    track: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun trackTop(period: Long): Query<TrackTop>

  public fun insert(
    type: Long,
    period: Long,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  ): Unit

  public fun deleteTop(type: Long, period: Long): Unit

  public fun upsert(
    itemId: Long?,
    playCount: Long?,
    type: Long,
    period: Long,
    rank: Int
  ): Unit
}
