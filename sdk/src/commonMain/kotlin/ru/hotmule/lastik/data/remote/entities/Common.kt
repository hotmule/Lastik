package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageAttributes(
    val page: String? = null,
    val perPage: String? = null,
    val user: String? = null,
    val total: String? = null,
    val totalPages: String? = null
)

@Serializable
data class LastFmItem(
    val date: Date? = null,
    val url: String? = null,
    val mbid: String? = null,
    val name: String? = null,
    val loved: Int? = null,
    val artist: MusicBrainzIdentifier? = null,
    val album: MusicBrainzIdentifier? = null,
    @SerialName("playcount") val playCount: Long? = null,
    @SerialName("@attr") val attributes: LastFmItemAttributes? = null,
    @SerialName("image") val images: List<Image>? = null,
)

@Serializable
data class LastFmItemAttributes(
    @SerialName("nowplaying") val nowPlaying: String? = null,
    val rank: Int? = null
)

@Serializable
data class Image(
    val size: String? = null,
    @SerialName("#text") val url: String? = null
)

@Serializable
data class Date(
    val uts: Long? = null,
    @SerialName("#text") val toSting: String? = null
)

@Serializable
data class UnixDate(
    @SerialName("unixtime") val time: String? = null
)

@Serializable
data class MusicBrainzIdentifier(
    val mbid: String? = null,
    val name: String? = null,
    @SerialName("#text") val text: String? = null
)