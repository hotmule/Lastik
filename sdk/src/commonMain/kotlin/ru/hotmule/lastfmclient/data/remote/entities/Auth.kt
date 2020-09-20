package ru.hotmule.lastfmclient.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("session") val params: SessionParams? = null
)

@Serializable
data class SessionParams(
    val name: String? = null,
    val key: String? = null,
    val subscriber: Int? = null
)