package ru.hotmule.lastfmclient.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scrobbles(
    val tracks: ResentTracks? = null
)

@Serializable
@SerialName("recenttracks")
data class ResentTracks(
    val attributes: Attributes? = null
)

@Serializable
@SerialName("@attr")
data class Attributes(
    val page: String? = null,
    val perPage: String? = null,
    val user: String? = null,
    val total: String? = null,
    val totalPages: String? = null
)