package ru.hotmule.lastfmclient.data.remote.entities

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val name: String? = null,
    val key: String? = null,
    val subscriber: Int? = null
)