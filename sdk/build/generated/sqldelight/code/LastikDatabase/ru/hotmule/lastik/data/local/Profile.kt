package ru.hotmule.lastik.data.local

import kotlin.Long
import kotlin.String

data class Profile(
  val nickname: String,
  val realName: String?,
  val lowResImage: String?,
  val highResImage: String?,
  val playCount: Long?,
  val registerDate: Long?
) {
  override fun toString(): String = """
  |Profile [
  |  nickname: $nickname
  |  realName: $realName
  |  lowResImage: $lowResImage
  |  highResImage: $highResImage
  |  playCount: $playCount
  |  registerDate: $registerDate
  |]
  """.trimMargin()
}
