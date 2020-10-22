package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Artist(
  val id: Long,
  val name: String,
  val rank: Int?,
  val playCount: Long?
) {
  override fun toString(): String = """
  |Artist [
  |  id: $id
  |  name: $name
  |  rank: $rank
  |  playCount: $playCount
  |]
  """.trimMargin()
}
