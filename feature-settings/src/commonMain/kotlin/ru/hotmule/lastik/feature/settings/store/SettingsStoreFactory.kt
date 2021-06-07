package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.settings.store.SettingsStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefs: PrefsStore
) {
    fun create(): SettingsStore = object : SettingsStore,
        Store<Intent, State, Nothing> by storeFactory.create(
            name = SettingsStore::class.simpleName,
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeAction(action: Unit, getState: () -> State) {
            dispatch(Result.AppsReceived(prefs.getApps()))
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CheckApp -> dispatch(Result.AppChecked(intent.id))
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.AppsReceived -> copy(apps = result.apps)
            is Result.AppChecked -> copy()
        }
    }
}