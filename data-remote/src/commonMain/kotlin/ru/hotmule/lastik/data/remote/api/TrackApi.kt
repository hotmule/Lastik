package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.Credentials
import ru.hotmule.lastik.data.remote.entities.NowPlayingResponse

class TrackApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
    private val credentials: Credentials
) {

    private fun HttpRequestBuilder.trackApi(
        method: String,
        parameters: Map<String, Any?>
    ) {
        api(
            secret = credentials.secret,
            params = parameters + mutableMapOf(
                "method" to "track.$method",
                "api_key" to credentials.apiKey,
                "sk" to prefs.sessionKey,
            ).apply {
                prefs.token?.let { set("token", it) }
                prefs.login?.let { set("username", it) }
                prefs.password?.let { set("password", it) }
            }
        )
    }

    suspend fun setLoved(
        track: String,
        artist: String,
        loved: Boolean
    ) = client.post<Any?> {
        trackApi(
            if (loved) "love" else "unlove",
            mapOf(
                "track" to track,
                "artist" to artist
            )
        )
    }

    suspend fun updateNowPlaying(
        track: String,
        artist: String,
        album: String? = null,
        duration: Long? = null,
        albumArtist: String? = null
    ) = client.post<NowPlayingResponse?> {
        trackApi(
            "updateNowPlaying",
            mapOf(
                "track" to track,
                "artist" to artist,
                "album" to album,
                "duration" to duration,
                "albumArtist" to albumArtist
            )
        )
    }
}