package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*

interface ShelfStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object RefreshItems : Intent()
        object LoadMoreItems : Intent()
        data class MakeLove(val title: String, val subtitle: String?, val isLoved: Boolean) : Intent()
    }

    sealed class Result {
        data class ItemsReceived(val items: List<ShelfItem>) : Result()
        data class Loading(val isLoading: Boolean, val isFirstPage: Boolean) : Result()
    }

    data class State(
        val items: List<ShelfItem> = listOf(),
        val isRefreshing: Boolean = false,
        val isMoreLoading: Boolean = false
    )
}