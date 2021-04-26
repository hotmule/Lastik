package ru.hotmule.lastik.feature.shelf.entity

data class ShelfItem(
    val highlighted: Boolean = false,
    val image: String = "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
    val title: String = "",
    val subtitle: String? = null,
    val hint: String? = null,
    val rank: Int? = null,
    val playCount: Long? = null,
    val loved: Boolean? = null
)