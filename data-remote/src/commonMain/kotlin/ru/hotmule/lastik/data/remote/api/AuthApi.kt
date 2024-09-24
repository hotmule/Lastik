package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.BuildKonfig
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient
) {

    private fun HttpRequestBuilder.authApi(
        method: String,
        parameters: Map<String, String?> = mapOf()
    ) {
        api(
            params = parameters + mutableMapOf(
                "method" to "auth.$method",
                "api_key" to BuildKonfig.apiKey
            ),
            secret = BuildKonfig.secret
        )
    }

    val authUrl = "http://www.last.fm/api/auth/" + "?" +
            "api_key=${BuildKonfig.apiKey}" + "&" +
            "cb=hotmule://lastik"

    suspend fun getSession(
        token: String
    ) = client.get {
        authApi(
            "getSession",
            mapOf("token" to token)
        )
    }.body<SessionResponse?>()

    suspend fun getMobileSession(
        login: String,
        password: String
    ) = client.post {
        authApi(
            "getMobileSession",
            mapOf(
                "username" to login,
                "password" to password
            )
        )
    }.body<SessionResponse?>()
}