package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.ColumnAdapter
import kotlin.Int
import kotlin.Long
import kotlin.String
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

public data class Top(
  public val id: Long,
  public val type: TopType,
  public val period: TopPeriod,
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

  public class Adapter(
    public val typeAdapter: ColumnAdapter<TopType, Long>,
    public val periodAdapter: ColumnAdapter<TopPeriod, Long>
  )
}
