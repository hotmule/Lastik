package ru.hotmule.lastik.feature.root.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.flow.collect
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.root.store.RootStore.Intent
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class RootStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val queries: ProfileQueries,
    private val onSessionChanged: (Boolean) -> Unit
) {
    fun create(): RootStore =
        object : RootStore, Store<Intent, Unit, Unit> by storeFactory.create(
            RootStore::class.simpleName,
            initialState = Unit,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, Unit, Unit, Unit>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeAction(
            action: Unit, getState: () -> Unit
        ) {
            prefsStore.isSessionActive.collect {
                if (!it) queries.deleteAll()
                onSessionChanged(it)
            }
        }

        override suspend fun executeIntent(
            intent: Intent, getState: () -> Unit
        ) {
            when (intent) {
                is Intent.ProcessUrl -> intent.url?.let {
                    if (it.contains("token")) {
                        prefsStore.token = it.substringAfter("token=")
                    }
                }
            }
        }
    }
}