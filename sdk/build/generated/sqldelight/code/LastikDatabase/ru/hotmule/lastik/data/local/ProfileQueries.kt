package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface ProfileQueries : Transacter {
  fun <T : Any> getProfile(mapper: (
    userName: String,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  fun getProfile(): Query<Profile>

  fun <T : Any> getProfileByNickname(userName: String, mapper: (
    userName: String,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  fun getProfileByNickname(userName: String): Query<Profile>

  fun <T : Any> getLowResImage(userName: String, mapper: (lowResImage: String?) -> T): Query<T>

  fun getLowResImage(userName: String): Query<GetLowResImage>

  fun deleteAll()

  fun upsert(
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?,
    userName: String
  )
}
