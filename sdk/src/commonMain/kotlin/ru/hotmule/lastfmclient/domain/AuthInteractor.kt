package ru.hotmule.lastfmclient.domain

import ru.hotmule.lastfmclient.data.prefs.PrefsSource
import ru.hotmule.lastfmclient.data.remote.api.AuthApi

class AuthInteractor(
    private val apiKey: String,
    private val secret: String,
    private val prefs: PrefsSource,
    private val api: AuthApi,
) {
    fun getAuthUrl() = "http://www.last.fm/api/auth/?api_key=$apiKey"

    suspend fun checkForToken(url: String) = if (url.contains("token")) {

        val token = url.substringAfter("token=")
        val session = api.getSession(
            apiKey,
            secret,
            url.substringAfter("token=")
        )

        prefs.token = token
        prefs.name = session?.name
        prefs.sessionKey = session?.key

        false
    } else
        true
}