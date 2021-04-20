package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
    private val apiKey: String,
    private val secret: String
) {

    private fun HttpRequestBuilder.authApi(
        method: String,
        parameters: Map<String, String> = mapOf()
    ) {
        api(
            params = parameters + mutableMapOf(
                "method" to "auth.$method",
                "api_key" to apiKey
            ).apply {
                prefs.token?.let { set("token", it) }
            },
            secret = secret
        )
    }

    suspend fun getSession() = client.get<SessionResponse?> {
        authApi("getSession")
    }

    suspend fun getMobileSession(
        login: String,
        password: String
    ) = client.post<SessionResponse?> {
        authApi(
            "getMobileSession",
            mapOf(
                "username" to login,
                "password" to password
            )
        )
    }
}