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
    @SerialName("track") val tracks: List<LastFmItem>? = null
)