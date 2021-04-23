package ru.hotmule.lastik.feature.library.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

class LibraryStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore
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
                is Intent.ChangeShelf -> dispatch(Result.ShelfSelected(
                    intent.index,
                    if (intent.index in 1..3) prefsStore.getShelfPeriod(intent.index) else null
                ))
                Intent.OpenPeriods -> dispatch(Result.PeriodsOpened)
                Intent.ClosePeriods -> dispatch(Result.PeriodsClosed)
                is Intent.SavePeriod -> savePeriod(getState().activeShelfIndex, intent.index)
                Intent.LogOut -> prefsStore.clear()
            }
        }

        private fun savePeriod(shelfIndex: Int, periodIndex: Int) {
            prefsStore.saveShelfPeriod(shelfIndex, periodIndex)
            dispatch(Result.PeriodSelected(periodIndex))
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = when (result) {
            is Result.ShelfSelected -> copy(
                activeShelfIndex = result.index,
                periodSelectable = result.periodIndex != null,
                selectedPeriodIndex = result.periodIndex ?: 0,
                logOutAllowed = result.index == 4
            )
            Result.PeriodsOpened -> copy(periodsOpened = true)
            Result.PeriodsClosed -> copy(periodsOpened = false)
            is Result.PeriodSelected -> copy(
                selectedPeriodIndex = result.index,
                periodsOpened = false
            )
        }
    }
}