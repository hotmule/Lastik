package ru.hotmule.lastik.feature.shelf

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.data.remote.api.UserApi

interface ShelfComponent {

    data class ShelfItem(
        val image: String = UserApi.defaultImageUrl,
        val title: String = "",
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