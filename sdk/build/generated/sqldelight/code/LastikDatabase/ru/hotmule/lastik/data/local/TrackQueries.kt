package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface TrackQueries : Transacter {
  public fun getLovedTracksPageCount(): Query<Long>

  public fun getId(artistId: Long, name: String): Query<Long>

  public fun <T : Any> lovedTracks(mapper: (
    artist: String?,
    track: String,
    loved: Boolean,
    lovedAt: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  public fun lovedTracks(): Query<LovedTracks>

  public fun insert(
    artistId: Long,
    albumId: Long?,
    name: String,
    loved: Boolean,
    lovedAt: Long?
  ): Unit

  public fun dropLovedTrackDates(): Unit

  public fun updateTrackLove(
    loved: Boolean,
    track: String,
    artist: String
  ): Unit

  public fun upsertRecentTrack(
    albumId: Long?,
    loved: Boolean,
    artistId: Long,
    name: String,
    lovedAt: Long?
  ): Unit

  public fun upsertLovedTrack(
    loved: Boolean,
    lovedAt: Long?,
    artistId: Long,
    name: String,
    albumId: Long?
  ): Unit
}
