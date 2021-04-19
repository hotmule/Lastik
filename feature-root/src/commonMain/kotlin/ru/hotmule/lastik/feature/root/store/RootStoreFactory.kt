package ru.hotmule.lastik.feature.root.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.root.store.RootStore.*

class RootStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefs: PrefsStore
) {

    fun create(): RootStore =
        object : RootStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = RootStore::class.simpleName,
            initialState = State,
            executorFactory = ::ExecutorImpl
        ) {}

    private inner class ExecutorImpl: SuspendExecutor<Intent, Nothing, State, Nothing, Nothing>() {

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {
            when (intent) {
                is Intent.TokenUrlReceived -> getTokenFromUrl(intent.url)
            }
        }

        private fun getTokenFromUrl(url: String) {

        }
    }
}