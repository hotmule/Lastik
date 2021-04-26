package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface ProfileQueries : Transacter {
  public fun <T : Any> getProfile(mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  public fun getProfile(): Query<Profile>

  public fun <T : Any> getFriends(parentUserName: String?, mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T>

  public fun getFriends(parentUserName: String?): Query<Profile>

  public fun deleteFriends(parentUserName: String?): Unit

  public fun deleteAll(): Unit

  public fun upsert(
    parentUsername: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?,
    userName: String,
    parentUserName: String?
  ): Unit
}
