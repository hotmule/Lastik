package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface PeriodQueries : Transacter {
  fun <T : Any> getPeriod(
    username: String,
    topTypeId: Long,
    mapper: (
      id: Long,
      username: String,
      topTypeId: Long,
      lengthId: Long
    ) -> T
  ): Query<T>

  fun getPeriod(username: String, topTypeId: Long): Query<Period>

  fun upsert(
    lengthId: Long,
    username: String,
    topTypeId: Long
  )
}
