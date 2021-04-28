package ru.hotmule.lastik.`data`.local

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class Top(
  public val id: Long,
  public val type: Long,
  public val period: Long,
  public val rank: Int,
  public val itemId: Long?,
  public val playCount: Long?
) {
  public override fun toString(): String = """
  |Top [
  |  id: $id
  |  type: $type
  |  period: $period
  |  rank: $rank
  |  itemId: $itemId
  |  playCount: $playCount
  |]
  """.trimMargin()
}
