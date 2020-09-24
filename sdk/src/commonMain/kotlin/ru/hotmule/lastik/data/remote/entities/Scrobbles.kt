package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scrobbles(
    @SerialName("recenttracks") val recent: Resent? = null
)

@Serializable
data class Resent(
    @SerialName("@attr") val attributes: RecentAttributes? = null,
    @SerialName("track") val tracks: List<Track>? = null
)