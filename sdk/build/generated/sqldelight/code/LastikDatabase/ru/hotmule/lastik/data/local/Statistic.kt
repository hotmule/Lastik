package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Statistic(
  val id: Long,
  val sectionId: Long?,
  val itemId: Long?,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Statistic [
  |  id: $id
  |  sectionId: $sectionId
  |  itemId: $itemId
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
