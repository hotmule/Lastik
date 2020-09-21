package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.settings.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi

class AuthInteractor(
    private val prefs: PrefsStore,
    private val api: AuthApi,
    private val apiKey: String,
    private val secret: String
) {

    fun isSessionActive() = prefs.isSessionActive

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
        prefs.token?.let { token ->
            api.getSession(secret, token).also {
                prefs.apply {
                    name = it?.params?.name
                    sessionKey = it?.params?.key
                    isSessionActive.value = true
                }
            }
        }
    }

    fun signOut() {
        prefs.apply {
            clear()
            isSessionActive.value = false
        }
    }
}