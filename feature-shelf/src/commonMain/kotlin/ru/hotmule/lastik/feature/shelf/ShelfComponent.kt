package ru.hotmule.lastik.feature.shelf

import kotlinx.coroutines.flow.Flow

interface ShelfComponent {

    companion object {
        const val defaultTitle = "UNKNOWN"
        const val defaultImageUrl = "https://lastfm.freetls.fastly.net/i/u/64s/" +
                "2a96cbd8b46e442fc41c2b86b821562f.png"
    }

    data class ShelfItem(
        val highlighted: Boolean = false,
        val image: String = defaultImageUrl,
        val title: String = defaultTitle,
        val subtitle: String? = null,
        val hint: String? = null,
        val rank: Int? = null,
        val playCount: Long? = null,
        val loved: Boolean? = null
    )

    data class Model(
        val items: List<ShelfItem> = listOf(),
        val isRefreshing: Boolean = false,
        val isMoreLoading: Boolean = false
    )

    val model: Flow<Model>

    fun onRefreshItems()

    fun onLoadMoreItems()

    fun onMakeLove(title: String, subtitle: String?, isLoved: Boolean)
}