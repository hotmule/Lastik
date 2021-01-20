package ru.hotmule.lastik.data.local

data class ListItem(
    val imageUrl: String? = null,
    val title: String? = null,
    val rank: Int? = null,
    val subtitle: String? = null,
    val playCount: Long? = null,
    val time: Long? = null,
    val loved: Boolean? = null,
    val nowPlaying: Boolean? = null,
    val onLike: ((Boolean) -> Unit)? = null
)