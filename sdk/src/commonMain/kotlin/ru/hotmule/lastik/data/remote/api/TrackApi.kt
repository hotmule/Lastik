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
        method: String
    ) {
        api("track", method, apiKey, prefs.token, secret, prefs.sessionKey)
    }

    suspend fun love(
        track: String,
        artist: String
    ) = client.post<Any?> {
        trackApi("love")
        parameter("track", track)
        parameter("artist", artist)
    }

    suspend fun unLove(
        track: String,
        artist: String
    ) = client.post<Any?> {
        trackApi("unlove")
        parameter("track", track)
        parameter("artist", artist)
    }
}