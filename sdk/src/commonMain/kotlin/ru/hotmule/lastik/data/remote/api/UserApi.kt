package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.*

class UserApi(
    private val client: HttpClient,
    private val apiKey: String
) {

    private fun HttpRequestBuilder.userApi(
        method: String
    ) {
        api("user", method, apiKey)
    }

    suspend fun getInfo(
        user: String?
    ) = client.get<ProfileResponse?> {
        userApi("getInfo")
        parameter("user", user)
    }

    suspend fun getFriends(
        user: String?
    ) = client.get<FriendsResponse?> {
        userApi("getFriends")
        parameter("user", user)
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

    suspend fun getTopAlbums(
        user: String?
    ) = client.get<AlbumsResponse?> {
        userApi("getTopAlbums")
        parameter("user", user)
    }

    suspend fun getTopTracks(
        user: String?
    ) = client.get<TopTracksResponse?> {
        userApi("getTopTracks")
        parameter("user", user)
    }

    suspend fun getLovedTracks(
        user: String?
    ) = client.get<LovedTracksResponse?> {
        userApi("getLovedTracks")
        parameter("user", user)
    }
}