package ru.hotmule.lastik.`data`.local

import kotlin.Long
import kotlin.String

public data class Artist(
  public val id: Long,
  public val name: String
) {
  public override fun toString(): String = """
  |Artist [
  |  id: $id
  |  name: $name
  |]
  """.trimMargin()
}
