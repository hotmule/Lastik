package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Long
import kotlin.String

interface PeriodQueries : Transacter {
  fun getPeriodId(username: String, topId: Long): Query<Long>

  fun upsert(
    lengthId: Long,
    username: String,
    topId: Long
  )
}
