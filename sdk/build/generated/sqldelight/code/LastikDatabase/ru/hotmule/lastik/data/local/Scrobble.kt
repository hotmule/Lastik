package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Scrobble(
  val trackId: String,
  val date: Long?,
  val nowPlaying: Boolean
) {
  override fun toString(): String = """
  |Scrobble [
  |  trackId: $trackId
  |  date: $date
  |  nowPlaying: $nowPlaying
  |]
  """.trimMargin()
}
