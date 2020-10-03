package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.local.Profile
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val userApi: UserApi,
    private val db: LastikDatabase,
): BaseInteractor(db) {

    fun observeInfo() = db.profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    suspend fun refreshInfo(
        name: String? = super.nickname
    ) {
        userApi.getInfo(name).also { response ->
            response?.user?.let {
                db.profileQueries.insert(
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