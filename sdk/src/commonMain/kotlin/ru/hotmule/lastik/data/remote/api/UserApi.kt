package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.entities.*

class UserApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
    private val apiKey: String
) {

    private fun HttpRequestBuilder.userApi(
        method: String,
        params: Map<String, Any?> = mapOf()
    ) {
        api(
            params + mapOf(
                "method" to "user.$method",
                "user" to prefs.name,
                "api_key" to apiKey
            )
        )
    }

    private fun HttpRequestBuilder.userApiPage(
        method: String,
        page: Int,
        param: Pair<String, Any?>? = null
    ) {
        userApi(
            method,
            mutableMapOf<String, Any?>("page" to page)
                .also { params -> param?.let { params[it.first] = it.second } }
        )
    }

    private fun HttpRequestBuilder.userApiLibraryPage(
        method: String,
        page: Int
    ) {
        userApiPage(method, page,
            //"period" to prefs.libraryPeriod
        )
    }

    suspend fun getInfo(
    ) = client.get<ProfileResponse?> {
        userApi("getInfo")
    }

    suspend fun getFriends(
        page: Int,
    ) = client.get<FriendsResponse?> {
        userApiPage("getFriends", page)
    }

    suspend fun getRecentTracks(
        page: Int
    ) = client.get<ScrobblesResponse?> {
        userApiPage("getRecentTracks", page, "extended" to 1)
    }

    suspend fun getTopArtists(
        page: Int
    ) = client.get<ArtistsResponse?> {
        userApiLibraryPage("getTopArtists", page)
    }

    suspend fun getTopAlbums(
        page: Int
    ) = client.get<AlbumsResponse?> {
        userApiLibraryPage("getTopAlbums", page)
    }

    suspend fun getTopTracks(
        page: Int
    ) = client.get<TopTracksResponse?> {
        userApiLibraryPage("getTopTracks", page)
    }

    suspend fun getLovedTracks(
        page: Int
    ) = client.get<LovedTracksResponse?> {
        userApiPage("getLovedTracks", page)
    }
}