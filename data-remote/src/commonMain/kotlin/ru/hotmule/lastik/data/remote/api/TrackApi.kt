package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.remote.BuildKonfig
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.entities.NowPlayingResponse

class TrackApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
) {

    private fun HttpRequestBuilder.trackApi(
        method: String,
        parameters: Map<String, Any?>
    ) {
        api(
            secret = BuildKonfig.secret,
            params = parameters + mutableMapOf(
                "method" to "track.$method",
                "api_key" to BuildKonfig.apiKey,
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
    ) = client.post {
        trackApi(
            if (loved) "love" else "unlove",
            mapOf(
                "track" to track,
                "artist" to artist
            )
        )
    }.body<Any?>()

    suspend fun updateNowPlaying(
        track: String,
        artist: String,
        album: String? = null,
        duration: Long? = null,
        albumArtist: String? = null
    ) = client.post {
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
    }.body<NowPlayingResponse?>()
}