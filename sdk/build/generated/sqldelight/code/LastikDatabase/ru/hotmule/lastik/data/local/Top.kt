package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.ColumnAdapter
import kotlin.Int
import kotlin.Long
import kotlin.String
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

data class Top(
  val id: Long,
  val type: TopType,
  val period: TopPeriod,
  val rank: Int,
  val itemId: Long?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Top [
  |  id: $id
  |  type: $type
  |  period: $period
  |  rank: $rank
  |  itemId: $itemId
  |  playCount: $playCount
  |]
  """.trimMargin()

  class Adapter(
    val typeAdapter: ColumnAdapter<TopType, Long>,
    val periodAdapter: ColumnAdapter<TopPeriod, Long>
  )
}
