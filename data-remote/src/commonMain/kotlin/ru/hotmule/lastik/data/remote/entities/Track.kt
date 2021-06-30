package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NowPlayingResponse(
    @SerialName("nowplaying") val nowPlaying: NowPlaying? = null
)

@Serializable
data class NowPlaying(
    val artist: NowPlayingItem? = null,
    val album: NowPlayingItem? = null,
    val track: NowPlayingItem? = null,
    val albumArtist: NowPlayingItem? = null,
    val ignoredMessage: IgnoredMessage? = null
)

@Serializable
data class NowPlayingItem(
    val corrected: String? = null,
    @SerialName("#text") val title: String? = null
)

@Serializable
data class IgnoredMessage(
    val code: String? = null,
    @SerialName("#text") val title: String? = null
)