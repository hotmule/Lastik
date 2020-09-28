package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Attributes(
  val id: Long,
  val name: String?,
  val playCount: Long?,
  val lowResImage: String?,
  val highResImage: String?
) {
  override fun toString(): String = """
  |Attributes [
  |  id: $id
  |  name: $name
  |  playCount: $playCount
  |  lowResImage: $lowResImage
  |  highResImage: $highResImage
  |]
  """.trimMargin()
}
