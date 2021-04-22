package ru.hotmule.lastik.feature.library.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

class LibraryStoreFactory(
    private val storeFactory: StoreFactory,
) {
    fun create(): LibraryStore =
        object : LibraryStore, Store<Intent, State, Nothing> by storeFactory.create(
            LibraryStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class  ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeShelf -> dispatch(Result.ShelfSelected(intent.index))
                Intent.OpenPeriods -> dispatch(Result.PeriodsOpened)
                Intent.ClosePeriods -> dispatch(Result.PeriodsClosed)
                is Intent.SavePeriod -> dispatch(Result.PeriodsClosed)
                Intent.LogOut -> { }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = when (result) {
            is Result.ShelfSelected -> copy(
                activeShelfIndex = result.index,
                periodSelectable = result.index in 1..3,
                logOutAllowed = result.index == 4
            )
            Result.PeriodsOpened -> copy(periodsOpened = true)
            Result.PeriodsClosed -> copy(periodsOpened = false)
        }
    }
}