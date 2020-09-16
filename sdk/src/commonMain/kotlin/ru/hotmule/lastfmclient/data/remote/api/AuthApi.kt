package ru.hotmule.lastfmclient.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastfmclient.data.remote.api

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
        token: String,
        secret: String,
    ) = client.get<Any?> {
        authApi("getSession", apiKey, token, secret)
    }
}