package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.Long
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

interface TopQueries : Transacter {
  fun insert(
    type: TopType,
    period: TopPeriod,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  )

  fun upsert(
    itemId: Long?,
    playCount: Long?,
    type: TopType,
    period: TopPeriod,
    rank: Int
  )
}
