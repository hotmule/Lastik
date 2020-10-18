package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.entities.User

class AuthInteractor(
    private val api: AuthApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore,
    private val apiKey: String
) : BaseInteractor(db, prefs) {

    fun getAuthUrl() = "http://www.last.fm/api/auth/?api_key=$apiKey"

    fun urlContainsToken(url: String?): Boolean {
        url?.let {
            if (url.contains("token")) {
                prefs.token = url.substringAfter("token=")
                return true
            }
        }
        return false
    }

    suspend fun getSessionKey() {
        api.getSession()?.also {
            prefs.apply {
                sessionKey = it.params?.key
                name = it.params?.name
                name?.let { insertUser(it) }
            }
        }
    }
}