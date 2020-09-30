package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Int
import kotlin.Long

interface StatisticQueries : Transacter {
  fun lastId(): Query<Long>

  fun insert(rank: Int?, playCount: Int?)
}
