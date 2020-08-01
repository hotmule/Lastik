package ru.hotmule.lastfmclient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scrobbles(
    val tracks: ResentTracks
)

@Serializable
@SerialName("recenttracks")
data class ResentTracks(
    val attributes: Attributes
)

@Serializable
@SerialName("@attr")
data class Attributes(
    val page: String?,
    val perPage: String?,
    val user: String?,
    val total: String,
    val totalPages: String
)