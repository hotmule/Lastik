package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
    @SerialName("#text") val title: String? = null
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