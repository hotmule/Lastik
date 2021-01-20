package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String

interface TrackQueries : Transacter {
  fun getLovedTracksPageCount(): Query<Long>

  fun getId(artistId: Long, name: String): Query<Long>

  fun <T : Any> lovedTracks(mapper: (
    artist: String?,
    track: String,
    loved: Boolean,
    lovedAt: Long?,
    lowArtwork: String?
  ) -> T): Query<T>

  fun lovedTracks(): Query<LovedTracks>

  fun insert(
    artistId: Long,
    albumId: Long?,
    name: String,
    loved: Boolean,
    lovedAt: Long?
  )

  fun dropLovedTrackDates()

  fun updateTrackLove(
    loved: Boolean,
    track: String,
    artist: String
  )

  fun upsertRecentTrack(
    albumId: Long?,
    loved: Boolean,
    artistId: Long,
    name: String,
    lovedAt: Long?
  )

  fun upsertLovedTrack(
    loved: Boolean,
    lovedAt: Long?,
    artistId: Long,
    name: String,
    albumId: Long?
  )
}
