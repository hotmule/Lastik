package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface ProfileQueries : Transacter {
  fun <T : Any> getProfile(mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  fun getProfile(): Query<Profile>

  fun <T : Any> getFriends(parentUserName: String?, mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  fun getFriends(parentUserName: String?): Query<Profile>

  fun deleteFriends(parentUserName: String?)

  fun deleteAll()

  fun upsert(
    parentUsername: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?,
    userName: String,
    parentUserName: String?
  )
}
