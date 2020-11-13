package ru.hotmule.lastik.domain

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

open class BaseInteractor {

    suspend fun providePage(
        currentItemsCount: Int,
        firstPage: Boolean,
        loadPage: suspend (Int) -> Unit
    ) {
        if (firstPage || currentItemsCount.rem(50) == 0) {
            loadPage.invoke(
                if (firstPage) 1 else currentItemsCount / 50 + 1
            )
        }
    }
}