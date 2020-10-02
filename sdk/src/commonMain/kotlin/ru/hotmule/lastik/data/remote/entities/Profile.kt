package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(val user: User)

@Serializable
data class User(
    val url: String? = null,
    val image: List<Image>? = null,
    val registered: UnixDate? = null,
    @SerialName("name") val nickname: String? = null,
    @SerialName("realname") val realName: String? = null,
    @SerialName("playcount") val playCount: Long? = null
)