package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface ProfileQueries : Transacter {
  fun <T : Any> getInfo(nickname: String, mapper: (
    nickname: String,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  fun getInfo(nickname: String): Query<Profile>

  fun <T : Any> getLowResImage(nickname: String, mapper: (lowResImage: String?) -> T): Query<T>

  fun getLowResImage(nickname: String): Query<GetLowResImage>

  fun insert(profile: Profile)
}
