package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class ShelfStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val userApi: UserApi,
    private val index: Int
) {

    fun create(): ShelfStore =
        object : ShelfStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "${ShelfStore::class.simpleName} $index",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {

        private val periodNames = arrayOf(
            "overall", "7day", "1month", "3month", "6month", "12month"
        )

        override suspend fun executeAction(
            action: Unit,
            getState: () -> State
        ) {

            val periodName = periodNames[prefsStore.getShelfPeriod(index)]

            when (index) {
                0 -> userApi.getRecentTracks(1)
                1 -> userApi.getTopArtists(1, periodName)
                2 -> userApi.getTopAlbums(1, periodName)
                3 -> userApi.getTopTracks(1, periodName)
                4 -> userApi.getLovedTracks(1)
            }
        }

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {

        }
    }

    object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = copy()
    }
}