package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.Long
import kotlin.String

interface AttributesQueries : Transacter {
  fun lastId(): Query<Long>

  fun insert(
    name: String?,
    rank: Int?,
    playCount: Int?,
    lowResImage: String?,
    highResImage: String?
  )
}
