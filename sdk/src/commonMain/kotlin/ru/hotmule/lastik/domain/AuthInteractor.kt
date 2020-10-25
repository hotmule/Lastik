package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi

class AuthInteractor(
    private val apiKey: String,
    private val api: AuthApi,
    private val prefs: PrefsStore,
    private val profileInteractor: ProfileInteractor
) {

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
                name?.let { profileInteractor.insertUser(it) }
            }
        }
    }
}