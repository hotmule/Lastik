package ru.hotmule.lastik.feature.menu.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.menu.store.MenuStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class MenuStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefs: PrefsStore
) {
    fun create(): MenuStore = object : MenuStore, Store<Intent, State, Nothing> by storeFactory.create(
        name = MenuStore::class.simpleName,
        initialState = State(),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {
            when (intent) {
                Intent.ProvideMenu -> dispatch(Result.MenuProvided)
                Intent.LogOut -> dispatch(Result.LoggingOut)
                Intent.LogOutCancel -> dispatch(Result.LoggingOutCanceled)
                Intent.LogOutConfirm -> prefs.clear()
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            Result.MenuProvided -> copy(isMenuOpened = !isMenuOpened)
            Result.LoggingOut -> copy(isLogOutShown = true)
            Result.LoggingOutCanceled -> copy(isLogOutShown = false)
        }
    }
}