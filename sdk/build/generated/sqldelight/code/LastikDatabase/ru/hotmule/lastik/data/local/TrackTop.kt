package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.String

data class TrackTop(
  val name: String?,
  val rank: Int?,
  val playCount: Int?,
  val lowResImage: String?
) {
  override fun toString(): String = """
  |TrackTop [
  |  name: $name
  |  rank: $rank
  |  playCount: $playCount
  |  lowResImage: $lowResImage
  |]
  """.trimMargin()
}
