package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.SessionResponse

class AuthApi(
    private val client: HttpClient,
    //private val prefs: PrefsSource,
    private val apiKey: String,
    private val secret: String
) {

    private fun HttpRequestBuilder.authApi(
        method: String
    ) {
        api(
            params = mapOf(
                "method" to "auth.$method",
                "api_key" to apiKey,
                //"token" to prefs.token,
            ),
            secret = secret
        )
    }

    fun getAuthUrl() = "http://www.last.fm/api/auth/" + "?" +
            "api_key=$apiKey" + "&" +
            "cb=hotmule://lastik"

    suspend fun getSession() = client.get<SessionResponse?> {
        authApi("getSession")
    }

    suspend fun getMobileSession() = client.get<SessionResponse?> {
        authApi("getMobileSession")
    }
}