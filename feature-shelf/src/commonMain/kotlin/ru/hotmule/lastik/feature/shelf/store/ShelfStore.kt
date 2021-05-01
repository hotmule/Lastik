package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.data.remote.entities.LibraryItem
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

    interface Repository {

        val items: Flow<List<ShelfItem>>

        suspend fun getItemsCount(): Int

        suspend fun provideItems(page: Int)

        suspend fun setTrackLove(artist: String, track: String, isLoved: Boolean)
    }
}