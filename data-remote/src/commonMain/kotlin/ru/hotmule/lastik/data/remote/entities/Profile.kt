package ru.hotmule.lastik.data.remote.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    @SerialName("session") val params: SessionParams? = null
)

@Serializable
data class SessionParams(
    val name: String? = null,
    val key: String? = null,
    val subscriber: Int? = null
)

@Serializable
data class FriendsResponse(val friends: Friends? = null)

@Serializable
data class Friends(val user: List<User>? = null)

@Serializable
data class ProfileResponse(val user: User? = null)

@Serializable
data class User(
    val url: String? = null,
    val image: List<Image>? = null,
    val registered: UnixDate? = null,
    @SerialName("name") val nickname: String? = null,
    @SerialName("realname") val realName: String? = null,
    @SerialName("playcount") val playCount: Long? = null
)