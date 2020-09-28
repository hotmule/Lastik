package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.ArtistsResponse
import ru.hotmule.lastik.data.remote.entities.ScrobblesResponse

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
    ) = client.get<ScrobblesResponse?> {
        userApi("getRecentTracks")
        parameter("user", user)
    }

    suspend fun getTopArtists(
        user: String?
    ) = client.get<ArtistsResponse?> {
        userApi("getTopArtists")
        parameter("user", user)
    }
}