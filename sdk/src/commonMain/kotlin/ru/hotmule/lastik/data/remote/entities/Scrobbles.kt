package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scrobbles(
    @SerialName("recenttracks") val recent: Resent? = null
)

@Serializable
data class Resent(
    @SerialName("@attr") val attributes: Attributes? = null,
    @SerialName("track") val tracks: List<Track>? = null
)

@Serializable
data class Attributes(
    val page: String? = null,
    val perPage: String? = null,
    val user: String? = null,
    val total: String? = null,
    val totalPages: String? = null
)

@Serializable
data class Track(
    val name: String? = null,
    val artist: Parameter? = null,
    val album: Parameter? = null,
    val streamable: Int? = null,
    val date: Date? = null,
    val url: String? = null,
    @SerialName("@attr") val nowPlaying: NowPlaying? = null,
    @SerialName("image") val images: List<Image>? = null,
)

@Serializable
data class NowPlaying(
    @SerialName("nowplaying") val isTrue: Boolean? = null
)