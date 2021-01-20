package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

interface TopQueries : Transacter {
  fun getTopCount(type: TopType, period: TopPeriod): Query<Long>

  fun <T : Any> artistTop(period: TopPeriod, mapper: (
    name: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  fun artistTop(period: TopPeriod): Query<ArtistTop>

  fun <T : Any> albumTop(period: TopPeriod, mapper: (
    artist: String?,
    album: String?,
    lowArtwork: String?,
    rank: Int,
    playCount: Long?
  ) -> T): Query<T>

  fun albumTop(period: TopPeriod): Query<AlbumTop>

  fun <T : Any> trackTop(period: TopPeriod, mapper: (
    artist: String?,
    track: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  fun trackTop(period: TopPeriod): Query<TrackTop>

  fun insert(
    type: TopType,
    period: TopPeriod,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  )

  fun deleteTop(type: TopType, period: TopPeriod)

  fun upsert(
    itemId: Long?,
    playCount: Long?,
    type: TopType,
    period: TopPeriod,
    rank: Int
  )
}
