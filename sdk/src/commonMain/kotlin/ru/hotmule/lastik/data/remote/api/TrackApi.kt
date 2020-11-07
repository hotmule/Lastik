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
            parameters = mapOf(
                "method" to "track.$method",
                "api_key" to apiKey,
                "token" to prefs.token,
                "sk" to prefs.sessionKey
            ) + parameters,
            secret = secret
        )
    }

    suspend fun love(
        track: String,
        artist: String
    ) = client.post<Any?> {
        trackApi(
            "love",
            mapOf(
                "track" to track,
                "artist" to artist
            )
        )
    }

    suspend fun unLove(
        track: String,
        artist: String
    ) = client.post<Any?> {
        trackApi(
            "unlove",
            mapOf(
                "track" to track,
                "artist" to artist
            )
        )
    }
}