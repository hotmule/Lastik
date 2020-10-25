package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.Long

interface StatisticQueries : Transacter {
  fun insert(
    sectionId: Long?,
    itemId: Long?,
    rank: Int?,
    playCount: Long?
  )

  fun deleteSectionTop(sectionId: Long?)
}
