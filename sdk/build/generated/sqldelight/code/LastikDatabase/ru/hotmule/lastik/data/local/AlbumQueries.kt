package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long
import kotlin.String

interface AlbumQueries : Transacter {
  fun getId(artistId: Long, name: String): Query<Long>

  fun insert(
    artistId: Long,
    name: String,
    lowArtwork: String?,
    highArtwork: String?
  )
}
