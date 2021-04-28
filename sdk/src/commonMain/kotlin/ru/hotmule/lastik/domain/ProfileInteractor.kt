package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val profileQueries: ProfileQueries
) {

    fun getName() = prefs.name

    fun observeInfo() = profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    fun observeFriends() = profileQueries
        .getFriends(prefs.name)
        .asFlow()
        .mapToList()

    suspend fun refreshProfile(
        isFirstPage: Boolean
    ) {
        refreshInfo()
        refreshFriends()
    }

    private suspend fun refreshInfo() {
        api.getInfo().also {
            it?.user?.let { user ->
                user.nickname?.let { name ->
                    insertUser(
                        nickname = name,
                        realName = user.realName,
                        lowResImage = user.image?.get(1)?.url,
                        highResImage = user.image?.get(2)?.url,
                        playCount = user.playCount,
                        registeredAt = user.registered?.time?.toLongOrNull()
                    )
                }
            }
        }
    }

    private suspend fun refreshFriends() {
        api.getFriends(1).also {
            profileQueries.transaction {
                profileQueries.deleteFriends(prefs.name)
                it?.friends?.user?.forEach {
                    it.nickname?.let { name ->
                        insertUser(
                            nickname = name,
                            parentUser = prefs.name,
                            lowResImage = it.image?.get(1)?.url,
                            highResImage = it.image?.get(2)?.url
                        )
                    }
                }
            }
        }
    }

    fun insertUser(
        nickname: String,
        parentUser: String? = null,
        realName: String? = null,
        lowResImage: String? = null,
        highResImage: String? = null,
        playCount: Long? = null,
        registeredAt: Long? = null
    ) {
        profileQueries.upsert(
            parentUser,
            realName,
            lowResImage,
            highResImage,
            playCount,
            registeredAt,
            nickname,
            parentUser
        )
    }
}