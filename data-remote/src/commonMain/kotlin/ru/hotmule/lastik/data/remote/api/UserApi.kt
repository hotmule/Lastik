package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.BuildKonfig
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.entities.*

class UserApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
) {

    companion object {

        const val defaultImageUrl = "https://lastfm.freetls.fastly.net/i/u/64s/" +
                "2a96cbd8b46e442fc41c2b86b821562f.png"

        private val periodNames = arrayOf(
            "overall", "12month", "6month", "3month", "1month", "7day"
        )
    }

    private fun HttpRequestBuilder.userApi(
        method: String,
        params: Map<String, Any?> = mapOf()
    ) {
        api(
            params + mapOf(
                "method" to "user.$method",
                "user" to "hotmu1e",
                "api_key" to BuildKonfig.apiKey
            )
        )
    }

    private fun HttpRequestBuilder.userApiPage(
        method: String,
        page: Int,
        param: Pair<String, Any>? = null
    ) {
        userApi(
            method,
            mutableMapOf<String, Any?>("page" to page).also { params ->
                param?.let { params[it.first] = it.second }
            }
        )
    }

    private fun HttpRequestBuilder.userApiLibraryPage(
        method: String,
        page: Int,
        period: String
    ) {
        userApiPage(method, page, "period" to period)
    }

    suspend fun getInfo(
    ) = client.get {
        userApi("getInfo")
    }.body<ProfileResponse?>()

    suspend fun getFriends(
        page: Int,
    ) = client.get {
        userApiPage("getFriends", page)
    }.body<FriendsResponse?>()

    suspend fun getScrobbles(
        page: Int
    ) = client.get {
        userApiPage("getRecentTracks", page, "extended" to 1)
    }.body<ScrobblesResponse?>()

    suspend fun getTopArtists(
        page: Int
    ) = client.get {
        userApiLibraryPage("getTopArtists", page, periodNames[prefs.artistsPeriod])
    }.body<ArtistsResponse?>()

    suspend fun getTopAlbums(
        page: Int
    ) = client.get {
        userApiLibraryPage("getTopAlbums", page, periodNames[prefs.albumsPeriod])
    }.body<AlbumsResponse?>()

    suspend fun getTopTracks(
        page: Int
    ) = client.get {
        userApiLibraryPage("getTopTracks", page, periodNames[prefs.tracksPeriod])
    }.body<TopTracksResponse?>()

    suspend fun getLovedTracks(
        page: Int
    ) = client.get {
        userApiPage("getLovedTracks", page)
    }.body<LovedTracksResponse?>()
}