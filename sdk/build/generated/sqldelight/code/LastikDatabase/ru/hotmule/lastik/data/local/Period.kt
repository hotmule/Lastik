package ru.hotmule.lastik.data.local

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Period(
  val id: Long,
  val username: String,
  val topId: Int,
  val lengthId: Int
) {
  override fun toString(): String = """
  |Period [
  |  id: $id
  |  username: $username
  |  topId: $topId
  |  lengthId: $lengthId
  |]
  """.trimMargin()
}
