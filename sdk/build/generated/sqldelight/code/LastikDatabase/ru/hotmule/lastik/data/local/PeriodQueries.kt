package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.String

interface PeriodQueries : Transacter {
  fun getPeriodLength(username: String, topId: Int): Query<Int>

  fun upsert(
    lengthId: Int,
    username: String,
    topId: Int
  )
}
