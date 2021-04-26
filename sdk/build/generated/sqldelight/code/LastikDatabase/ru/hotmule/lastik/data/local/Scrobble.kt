package ru.hotmule.lastik.`data`.local

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class Scrobble(
  public val id: Long,
  public val trackId: Long,
  public val listenedAt: Long,
  public val nowPlaying: Boolean
) {
  public override fun toString(): String = """
  |Scrobble [
  |  id: $id
  |  trackId: $trackId
  |  listenedAt: $listenedAt
  |  nowPlaying: $nowPlaying
  |]
  """.trimMargin()
}
