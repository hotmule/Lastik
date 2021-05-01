package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScrobblesResponse(
    @SerialName("recenttracks") val recent: ResentTracks? = null
)

@Serializable
data class ResentTracks(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("track") val tracks: List<LibraryItem>? = null
)

@Serializable
data class ArtistsResponse(
    @SerialName("topartists") val top: TopArtists? = null,
)

@Serializable
data class TopArtists(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("artist") val artists: List<LibraryItem>? = null
)

@Serializable
data class AlbumsResponse(
    @SerialName("topalbums") val top: TopAlbums? = null,
)

@Serializable
data class TopAlbums(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("album") val albums: List<LibraryItem>? = null
)

@Serializable
data class TopTracksResponse(
    @SerialName("toptracks") val top: Tracks? = null
)

@Serializable
data class LovedTracksResponse(
    @SerialName("lovedtracks") val loved: Tracks? = null
)

@Serializable
data class Tracks(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("track") val tracks: List<LibraryItem>? = null
)

@Serializable
data class PageAttributes(
    val page: String? = null,
    val perPage: String? = null,
    val user: String? = null,
    val total: String? = null,
    val totalPages: String? = null
)

@Serializable
data class LibraryItem(
    var date: Date? = null,
    val url: String? = null,
    val mbid: String? = null,
    val name: String? = null,
    val loved: Int? = null,
    val artist: MusicBrainzIdentifier? = null,
    val album: MusicBrainzIdentifier? = null,
    @SerialName("playcount") val playCount: Long? = null,
    @SerialName("@attr") val attributes: LibraryItemAttributes? = null,
    @SerialName("image") val images: List<Image>? = null,
)

@Serializable
data class LibraryItemAttributes(
    @SerialName("nowplaying") val nowPlaying: String? = null,
    val rank: Int? = null
)

@Serializable
data class MusicBrainzIdentifier(
    val mbid: String? = null,
    val name: String? = null,
    @SerialName("#text") val text: String? = null
)