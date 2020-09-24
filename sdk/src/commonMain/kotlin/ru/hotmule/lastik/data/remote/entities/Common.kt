package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
data class MusicBrainzIdentifier(
    @SerialName("mbid") val id: String? = null,
    @SerialName("#text") val name: String? = null
)