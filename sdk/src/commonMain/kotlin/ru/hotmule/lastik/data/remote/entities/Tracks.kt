package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    @SerialName("track") val list: List<LastFmItem>? = null
)