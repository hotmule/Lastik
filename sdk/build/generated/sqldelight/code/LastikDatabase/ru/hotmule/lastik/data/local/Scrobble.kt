package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Scrobble(
  val id: Long,
  val trackId: Long,
  val uts: Long?,
  val date: String?,
  val nowPlaying: Boolean
) {
  override fun toString(): String = """
  |Scrobble [
  |  id: $id
  |  trackId: $trackId
  |  uts: $uts
  |  date: $date
  |  nowPlaying: $nowPlaying
  |]
  """.trimMargin()
}
