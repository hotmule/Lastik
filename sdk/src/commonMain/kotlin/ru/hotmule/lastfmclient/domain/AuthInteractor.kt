package ru.hotmule.lastfmclient.domain

import kotlinx.coroutines.flow.MutableStateFlow
import ru.hotmule.lastfmclient.data.prefs.PrefsSource
import ru.hotmule.lastfmclient.data.remote.api.AuthApi

class AuthInteractor(
    private val apiKey: String,
    private val secret: String,
    private val prefs: PrefsSource,
    private val api: AuthApi,
) {

    val isSessionActive = MutableStateFlow(prefs.sessionKey != null)

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

    suspend fun signIn() {
        prefs.token?.let { token ->
            val session = api.getSession(apiKey, secret, token)
            prefs.name = session?.name
            prefs.sessionKey = session?.key
            isSessionActive.value = true
        }
    }

    fun signOut() {
        prefs.clear()
        isSessionActive.value = false
    }
}