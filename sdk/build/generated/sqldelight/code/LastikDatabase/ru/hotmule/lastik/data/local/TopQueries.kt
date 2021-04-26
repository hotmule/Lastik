package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

public interface TopQueries : Transacter {
  public fun getTopCount(type: TopType, period: TopPeriod): Query<Long>

  public fun <T : Any> artistTop(period: TopPeriod, mapper: (
    name: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun artistTop(period: TopPeriod): Query<ArtistTop>

  public fun <T : Any> albumTop(period: TopPeriod, mapper: (
    artist: String?,
    album: String?,
    lowArtwork: String?,
    rank: Int,
    playCount: Long?
  ) -> T): Query<T>

  public fun albumTop(period: TopPeriod): Query<AlbumTop>

  public fun <T : Any> trackTop(period: TopPeriod, mapper: (
    artist: String?,
    track: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun trackTop(period: TopPeriod): Query<TrackTop>

  public fun insert(
    type: TopType,
    period: TopPeriod,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  ): Unit

  public fun deleteTop(type: TopType, period: TopPeriod): Unit

  public fun upsert(
    itemId: Long?,
    playCount: Long?,
    type: TopType,
    period: TopPeriod,
    rank: Int
  ): Unit
}
