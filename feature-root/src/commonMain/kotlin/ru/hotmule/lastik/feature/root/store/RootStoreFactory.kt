package ru.hotmule.lastik.feature.root.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
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

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, Unit, Unit, Unit>(
        AppCoroutineDispatcher.Main
    ) {
        override fun executeAction(action: Unit) {
            scope.launch {
                prefsStore.isSessionActive.collect {
                    if (!it) queries.deleteAll()
                    onSessionChanged(it)
                }
            }
        }

        override fun executeIntent(intent: Intent) {
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