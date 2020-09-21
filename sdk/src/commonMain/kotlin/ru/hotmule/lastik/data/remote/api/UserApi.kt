package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.Scrobbles

class UserApi(
    private val client: HttpClient,
    private val apiKey: String
) {

    private fun HttpRequestBuilder.userApi(
        method: String
    ) {
        api("user", method, apiKey)
    }

    suspend fun getRecentTracks(
        user: String?
    ) = client.get<Scrobbles?> {
        userApi("getRecentTracks")
        parameter("user", user)
    }
}