package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class ShelfStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShelfStoreRepository
) {

    fun create(): ShelfStore = object : ShelfStore, Store<Intent, State, Nothing> by storeFactory.create(
        name = "${ShelfStore::class.simpleName}",
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {

        override fun executeAction(action: Unit) {
            scope.launch {
                withContext(AppCoroutineDispatcher.Main) {
                    launch { collectItems() }
                    launch { loadPage(true) }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            scope.launch {
                when (intent) {
                    Intent.RefreshItems -> loadPage(true)
                    Intent.LoadMoreItems -> loadPage(false)
                    is Intent.MakeLove -> makeLove(
                        artist = intent.subtitle ?: "",
                        track = intent.title,
                        isLoved = !intent.isLoved
                    )
                }
            }
        }

        private suspend fun collectItems() {
            repository.items.collect {
                dispatch(Result.ItemsReceived(it))
            }
        }

        private suspend fun loadPage(isFirst: Boolean) {

            val count = repository.getItemsCount()

            val page = when {
                isFirst -> 1
                count.rem(50) == 0 -> count / 50 + 1
                else -> null
            }

            if (page != null) {
                try {
                    dispatch(Result.Loading(isLoading = true, isFirstPage = isFirst))
                    repository.provideItems(page)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    dispatch(Result.Loading(isLoading = false, isFirstPage = isFirst))
                }
            }
        }

        private suspend fun makeLove(artist: String, track: String, isLoved: Boolean) {
            try {
                repository.setTrackLove(artist, track, isLoved)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(msg: Result): State = when (msg) {
            is Result.ItemsReceived -> copy(items = msg.items)
            is Result.Loading -> copy(
                isRefreshing = msg.isLoading && msg.isFirstPage,
                isMoreLoading = msg.isLoading && !msg.isFirstPage
            )
        }
    }
}