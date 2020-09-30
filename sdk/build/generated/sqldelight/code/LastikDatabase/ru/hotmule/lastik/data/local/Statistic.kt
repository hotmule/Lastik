package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Statistic(
  val id: Long,
  val rank: Int?,
  val playCount: Int?
) {
  override fun toString(): String = """
  |Statistic [
  |  id: $id
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
