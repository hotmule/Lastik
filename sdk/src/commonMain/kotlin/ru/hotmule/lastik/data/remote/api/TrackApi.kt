package ru.hotmule.lastik.data.remote.api

import io.ktor.client.*
import io.ktor.client.request.*
import ru.hotmule.lastik.data.prefs.PrefsStore

class TrackApi(
    private val client: HttpClient,
    private val prefs: PrefsStore,
    private val apiKey: String,
    private val secret: String
) {

    private fun HttpRequestBuilder.trackApi(
        method: String,
        parameters: Map<String, String>
    ) {
        api(
            params = parameters + mapOf(
                "method" to "track.$method",
                "api_key" to apiKey,
                "token" to prefs.token,
                "sk" to prefs.sessionKey,
            ),
            secret = secret
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
}