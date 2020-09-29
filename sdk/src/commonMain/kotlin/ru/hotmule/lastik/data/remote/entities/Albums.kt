package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumsResponse(
    @SerialName("topalbums") val top: TopAlbums? = null,
)

@Serializable
data class TopAlbums(
    @SerialName("@attr") val attributes: PageAttributes? = null,
    @SerialName("album") val albums: List<LastFmItem>? = null
)