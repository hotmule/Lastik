package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentAttributes(
    val page: String? = null,
    val perPage: String? = null,
    val user: String? = null,
    val total: String? = null,
    val totalPages: String? = null
)

@Serializable
data class Track(
    val name: String? = null,
    val artist: MusicBrainzIdentifier? = null,
    val album: MusicBrainzIdentifier? = null,
    val streamable: Int? = null,
    val date: Date? = null,
    val url: String? = null,
    @SerialName("mbid") val id: String? = null,
    @SerialName("@attr") val attributes: TrackAttributes? = null,
    @SerialName("image") val images: List<Image>? = null,
)

@Serializable
data class TrackAttributes(
    @SerialName("nowplaying") val nowPlaying: Boolean? = null
)