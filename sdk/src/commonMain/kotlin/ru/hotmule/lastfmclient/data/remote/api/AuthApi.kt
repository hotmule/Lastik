package ru.hotmule.lastfmclient.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastfmclient.data.remote.api
import ru.hotmule.lastfmclient.data.remote.entities.Session

class AuthApi(
    private val client: HttpClient
) {

    private fun HttpRequestBuilder.authApi(
        method: String,
        apiKey: String,
        token: String,
        secret: String
    ) {
        api("auth", method, apiKey, token, secret)
    }

    suspend fun getSession(
        apiKey: String,
        secret: String,
        token: String
    ) = client.get<Session?> {
        authApi("getSession", apiKey, token, secret)
    }
}