package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.entities.*

class UserApi(
    private val client: HttpClient,
    private val apiKey: String
) {

    private fun HttpRequestBuilder.userApi(
        method: String,
        user: String?
    ) {
        api("user", method, apiKey)
        parameter("user", user)
    }

    private fun HttpRequestBuilder.userApiPage(
        method: String,
        user: String?,
        page: Int
    ) {
        userApi(method, user)
        parameter("page", page)
    }

    private fun HttpRequestBuilder.libraryPage(
        method: String,
        user: String?,
        page: Int,
        period: LibraryPeriod = LibraryPeriod.Overall
    ) {
        userApiPage(method, user, page)
        parameter("period", period.value)
    }

    suspend fun getInfo(
        user: String?
    ) = client.get<ProfileResponse?> {
        userApi("getInfo", user)
    }

    suspend fun getFriends(
        user: String?,
        page: Int,
    ) = client.get<FriendsResponse?> {
        userApiPage("getFriends", user, page)
    }

    suspend fun getRecentTracks(
        user: String?,
        page: Int
    ) = client.get<ScrobblesResponse?> {
        userApiPage("getRecentTracks", user, page)
        parameter("extended", 1)
    }

    suspend fun getTopArtists(
        user: String?,
        page: Int
    ) = client.get<ArtistsResponse?> {
        libraryPage("getTopArtists", user, page)
    }

    suspend fun getTopAlbums(
        user: String?,
        page: Int
    ) = client.get<AlbumsResponse?> {
        libraryPage("getTopAlbums", user, page)
    }

    suspend fun getTopTracks(
        user: String?,
        page: Int
    ) = client.get<TopTracksResponse?> {
        libraryPage("getTopTracks", user, page)
    }

    suspend fun getLovedTracks(
        user: String?,
        page: Int
    ) = client.get<LovedTracksResponse?> {
        userApiPage("getLovedTracks", user, page)
    }
}