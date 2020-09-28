package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long

interface AlbumQueries : Transacter {
  fun lastId(): Query<Long>

  fun insert(artistId: Long, attrsId: Long)

  fun deleteAll()
}
