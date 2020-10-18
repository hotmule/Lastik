package ru.hotmule.lastik.data.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

data class Scrobble(
  val id: Long,
  val trackId: Long,
  val listenedAt: Long,
  val nowPlaying: Boolean
) {
  override fun toString(): String = """
  |Scrobble [
  |  id: $id
  |  trackId: $trackId
  |  listenedAt: $listenedAt
  |  nowPlaying: $nowPlaying
  |]
  """.trimMargin()
}
