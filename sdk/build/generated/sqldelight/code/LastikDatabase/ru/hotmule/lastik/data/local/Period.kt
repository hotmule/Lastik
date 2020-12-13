package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Period(
  val id: Long,
  val username: String,
  val topTypeId: Long,
  val lengthId: Long
) {
  override fun toString(): String = """
  |Period [
  |  id: $id
  |  username: $username
  |  topTypeId: $topTypeId
  |  lengthId: $lengthId
  |]
  """.trimMargin()
}
