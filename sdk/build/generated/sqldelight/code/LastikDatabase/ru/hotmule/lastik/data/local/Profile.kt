package ru.hotmule.lastik.`data`.local

import kotlin.Long
import kotlin.String

public data class Profile(
  public val userName: String,
  public val parentUserName: String?,
  public val realName: String?,
  public val lowResImage: String?,
  public val highResImage: String?,
  public val playCount: Long?,
  public val registerDate: Long?
) {
  public override fun toString(): String = """
  |Profile [
  |  userName: $userName
  |  parentUserName: $parentUserName
  |  realName: $realName
  |  lowResImage: $lowResImage
  |  highResImage: $highResImage
  |  playCount: $playCount
  |  registerDate: $registerDate
  |]
  """.trimMargin()
}
