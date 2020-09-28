package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistsResponse(
    @SerialName("topartists") val top: TopArtists? = null,
)

@Serializable
data class TopArtists(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("artist") val artists: List<LastFmItem>? = null
)