package ru.hotmule.lastik.data.local

import kotlin.String

data class GetLowResImage(
  val lowResImage: String?
) {
  override fun toString(): String = """
  |GetLowResImage [
  |  lowResImage: $lowResImage
  |]
  """.trimMargin()
}
