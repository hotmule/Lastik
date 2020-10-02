package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import ru.hotmule.lastik.data.local.Profile
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val prefs: PrefsStore,
    private val userApi: UserApi,
    private val profileQueries: ProfileQueries,
) {
    fun getNickname() = prefs.name!!

    fun observeInfo() = profileQueries
        .getInfo(prefs.name!!)
        .asFlow()
        .mapToOneOrNull()

    suspend fun refreshInfo(
        name: String? = prefs.name
    ) {
        userApi.getInfo(name).also { response ->
            response?.user?.let {
                profileQueries.insert(
                    Profile(
                        name!!,
                        it.realName,
                        it.image?.get(1)?.url,
                        it.image?.get(2)?.url,
                        it.playCount,
                        it.registered?.time?.toLongOrNull()
                    )
                )
            }
        }
    }
}