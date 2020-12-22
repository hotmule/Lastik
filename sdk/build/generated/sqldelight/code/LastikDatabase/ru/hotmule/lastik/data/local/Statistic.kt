package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Statistic(
  val id: Long,
  val periodId: Long,
  val rank: Int,
  val itemId: Long?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Statistic [
  |  id: $id
  |  periodId: $periodId
  |  rank: $rank
  |  itemId: $itemId
  |  playCount: $playCount
  |]
  """.trimMargin()
}