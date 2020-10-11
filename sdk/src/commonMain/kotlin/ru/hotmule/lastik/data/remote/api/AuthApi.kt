package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient,
    private val apiKey: String
) {

    private fun HttpRequestBuilder.authApi(
        method: String,
        token: String,
        secret: String
    ) {
        api("auth", method, apiKey, token, secret)
    }

    suspend fun getSession(
        secret: String,
        token: String
    ) = client.get<SessionResponse?> {
        authApi("getSession", token, secret)
    }
}