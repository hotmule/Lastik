package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.Long

interface StatisticQueries : Transacter {
  fun insert(
    periodId: Long,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  )

  fun upsert(
    itemId: Long?,
    playCount: Long?,
    periodId: Long,
    rank: Int
  )
}
