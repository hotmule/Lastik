package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.local.Profile
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.User

class ProfileInteractor(
    private val userApi: UserApi,
    private val db: LastikDatabase,
): BaseInteractor(db) {

    fun observeInfo() = db.profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    fun observeFriends() = db.profileQueries
        .getFriends(getUserName())
        .asFlow()
        .mapToList()

    suspend fun refreshProfile(
        name: String? = getUserName()
    ) {
        val infoResponse = userApi.getInfo(name)
        val friendsResponse = userApi.getFriends(name)
        db.transaction {
            infoResponse?.user?.let { insertUser(it) }
            db.profileQueries.deleteFriends(getUserName())
            friendsResponse?.friends?.user?.forEach {
                insertUser(
                    it,
                    getUserName()
                )
            }
        }
    }
}