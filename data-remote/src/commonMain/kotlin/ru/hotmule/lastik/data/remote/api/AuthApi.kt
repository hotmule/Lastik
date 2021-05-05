package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.Credentials
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient,
    private val credentials: Credentials
) {

    private fun HttpRequestBuilder.authApi(
        method: String,
        parameters: Map<String, String?> = mapOf()
    ) {
        api(
            params = parameters + mutableMapOf(
                "method" to "auth.$method",
                "api_key" to credentials.apiKey
            ),
            secret = credentials.secret
        )
    }

    val authUrl = "http://www.last.fm/api/auth/" + "?" +
            "api_key=${credentials.apiKey}" + "&" +
            "cb=hotmule://lastik"

    suspend fun getSession(
        token: String
    ) = client.get<SessionResponse?> {
        authApi(
            "getSession",
            mapOf("token" to token)
        )
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